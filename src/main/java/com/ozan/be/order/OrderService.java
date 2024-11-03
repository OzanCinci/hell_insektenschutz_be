package com.ozan.be.order;

import static com.ozan.be.order.domain.PaymentMethod.CREDIT_CART;
import static com.ozan.be.order.domain.PaymentMethod.PAY_WITH_BANK_TRANSFER;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.ozan.be.aws.S3InvoicePdfsService;
import com.ozan.be.aws.SqsMessagePublisher;
import com.ozan.be.customException.types.BadRequestException;
import com.ozan.be.customException.types.DataNotFoundException;
import com.ozan.be.mail.MailService;
import com.ozan.be.mail.domain.MailType;
import com.ozan.be.order.domain.OrderStatus;
import com.ozan.be.order.domain.PaymentMethod;
import com.ozan.be.order.dto.CreateOrderItemResponseDTO;
import com.ozan.be.order.dto.CreateOrderRequestDTO;
import com.ozan.be.order.dto.CreateOrderResponseDTO;
import com.ozan.be.order.dto.CreateOrderVisitorRequestDTO;
import com.ozan.be.order.dto.OrderInvoiceDTO;
import com.ozan.be.order.dto.OrderSingleItemRequestDTO;
import com.ozan.be.order.dto.UpdateCargoInfoRequestDTO;
import com.ozan.be.product.Product;
import com.ozan.be.product.ProductService;
import com.ozan.be.transactions.TransactionRecord;
import com.ozan.be.transactions.TransactionRecordService;
import com.ozan.be.user.User;
import com.ozan.be.user.service.UserService;
import com.ozan.be.utils.ModelMapperUtils;
import com.ozan.be.utils.PageableUtils;
import com.ozan.be.utils.UniqueCodeGenerator;
import com.querydsl.core.types.Predicate;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserService userService;
  private final ProductService productService;
  private final TransactionRecordService transactionRecordService;
  private final MailService mailService;
  private final SqsMessagePublisher sqsMessagePublisher;
  private final S3InvoicePdfsService s3InvoicePdfsService;

  public Page<CreateOrderResponseDTO> getAllOrders(Pageable pageable, Predicate filter) {
    Pageable finalPageable = PageableUtils.prepareAuditSorting(pageable);
    return buildCreateOrderResponseDTO(filter, finalPageable);
  }

  private Order findOrderByIdThrowsException(UUID id) {
    return orderRepository
        .findById(id)
        .orElseThrow(() -> new DataNotFoundException("No order found with id: " + id));
  }

  private Order findOrderByTraceCodeThrowsException(String traceCode) {
    return orderRepository
        .findByTraceCode(traceCode)
        .orElseThrow(
            () -> new DataNotFoundException("No order found with trace code: " + traceCode));
  }

  public void updateOrderStatus(
      UUID id, OrderStatus orderStatus, UpdateCargoInfoRequestDTO cargoInfoRequestDTO) {
    Order order = findOrderByIdThrowsException(id);

    if (order.getOrderStatus().equals(OrderStatus.PENDING_PAYMENT)
        && OrderStatus.ACTIVE.equals(orderStatus)) {
      handleOrderStatusApproveAsyncEvents(order);
    }

    // ask business decision before going live
    // validateOrderStatusChange(order.getOrderStatus(), orderStatus);

    order.setOrderStatus(orderStatus);
    order.setCargoCode(cargoInfoRequestDTO.getCargoCode());
    orderRepository.saveAndFlush(order);
  }

  private void validateOrderStatusChange(OrderStatus orderStatus, OrderStatus newOrderStatus) {
    List<OrderStatus> orderedStatus = OrderStatus.getSortedOrderStatusNames();
    if (orderedStatus.indexOf(orderStatus) >= orderedStatus.indexOf(newOrderStatus)) {
      throw new BadRequestException("Invalid order status request for the current order.");
    }
  }

  @Transactional
  public CreateOrderResponseDTO createOrdersAuthUser(
      UUID userId, CreateOrderRequestDTO requestDTO) {
    User user = userService.getUserById(userId);

    Order order = ModelMapperUtils.map(requestDTO.getAddress(), Order.class);
    order = ModelMapperUtils.mapToExisting(requestDTO.getCart(), order);

    PaymentMethod paymentMethod =
        isNull(requestDTO.getTransactionID()) ? PAY_WITH_BANK_TRANSFER : CREDIT_CART;
    order.setPaymentMethod(paymentMethod);

    order.setOrderStatus(requestDTO.getOrderStatus());
    order.setUserEmail(user.getEmail());
    order.setUserName(user.getFirstName() + " " + user.getLastName());
    order.setUser(user);

    Set<UUID> requiredProductIds =
        requestDTO.getCart().getItems().stream()
            .map(OrderSingleItemRequestDTO::getProductID)
            .collect(Collectors.toSet());
    Map<UUID, Product> productMap = productService.findByIdInAndMapByUUID(requiredProductIds);

    List<OrderItem> orderItems =
        requestDTO.getCart().getItems().stream()
            .map(
                oi -> {
                  if (!productMap.containsKey(oi.getProductID())) {
                    throw new BadRequestException("No product found with id: " + oi.getProductID());
                  }

                  OrderItem orderItem = ModelMapperUtils.map(oi, OrderItem.class);
                  Product product = productMap.get(oi.getProductID());
                  orderItem.setProduct(product);

                  return orderItem;
                })
            .toList();

    // Ensure orderItems is not null and not empty
    if (orderItems != null && !orderItems.isEmpty()) {
      for (OrderItem orderItem : orderItems) {
        orderItem.setOrder(order);
      }
      order.getOrderItems().addAll(orderItems);
    }
    setUniqueTraceCode(order);
    order.setIsInvoiceGenerated(false);

    Order savedOrder = orderRepository.saveAndFlush(order);

    // SEND USER AN E-MAIL
    try {
      mailService.sendHtmlEmail(savedOrder.getUser(), MailType.CREATE_ORDER, savedOrder);
    } catch (Exception e) {
      // log fail...
    }

    // UPDATE TRANSACTION
    if (nonNull(requestDTO.getTransactionID())) {
      TransactionRecord transactionRecord =
          transactionRecordService.findTransactionRecordByIdThrowsException(
              requestDTO.getTransactionID());
      transactionRecord.setTraceCode(savedOrder.getTraceCode());
      transactionRecordService.saveAndFlush(transactionRecord);
    }

    CreateOrderResponseDTO responseDTO =
        ModelMapperUtils.map(savedOrder, CreateOrderResponseDTO.class);
    List<CreateOrderItemResponseDTO> orderItemResponseDTOS =
        ModelMapperUtils.mapAll(savedOrder.getOrderItems(), CreateOrderItemResponseDTO.class);
    responseDTO.setOrderItems(orderItemResponseDTOS);

    handleOrderCreateAsyncEvents(savedOrder);
    return responseDTO;
  }

  public boolean isInvoiceAlreadyExists(String unprocessedTraceCode) {
    String traceCode = unprocessedTraceCode.replace("-", "");
    Order order = findOrderByTraceCodeThrowsException(traceCode);
    return Boolean.TRUE.equals(order.getIsInvoiceGenerated());
  }

  private void handleOrderCreateAsyncEvents(Order savedOrder) {
    if (CREDIT_CART.equals(savedOrder.getPaymentMethod())) {
      OrderInvoiceDTO orderInvoiceDTO = new OrderInvoiceDTO(savedOrder);
      sqsMessagePublisher.sendMessageToSqs(
          orderInvoiceDTO, "InvoiceGenerator", orderInvoiceDTO.getOrder().getOrderNumber());
    }
  }

  private void handleOrderStatusApproveAsyncEvents(Order order) {
    OrderInvoiceDTO orderInvoiceDTO = new OrderInvoiceDTO(order);
    sqsMessagePublisher.sendMessageToSqs(
        orderInvoiceDTO, "InvoiceGenerator", orderInvoiceDTO.getOrder().getOrderNumber());
  }

  @Transactional
  public void updateInvoiceStatusOfOrderByTraceCode(String unprocessedTraceCode) {
    String traceCode = unprocessedTraceCode.replace("-", "");
    Order order = findOrderByTraceCodeThrowsException(traceCode);
    order.setIsInvoiceGenerated(true);
    orderRepository.saveAndFlush(order);
  }

  private void setUniqueTraceCode(Order order) {
    String traceCode;
    do {
      traceCode = UniqueCodeGenerator.generateUniqueCode(9); // Adjust length as needed
    } while (orderRepository.existsByTraceCode(traceCode));
    order.setTraceCode(traceCode);
  }

  public CreateOrderResponseDTO getOrderByTraceCode(String traceCode) {
    Order order = findOrderByTraceCodeThrowsException(traceCode);
    CreateOrderResponseDTO responseDTO = ModelMapperUtils.map(order, CreateOrderResponseDTO.class);
    List<CreateOrderItemResponseDTO> orderItemResponseDTOS =
        ModelMapperUtils.mapAll(order.getOrderItems(), CreateOrderItemResponseDTO.class);
    responseDTO.setOrderItems(orderItemResponseDTOS);
    return responseDTO;
  }

  public Page<CreateOrderResponseDTO> getOrdersByUserId(Pageable pageable, UUID userId) {
    Predicate filter = QOrder.order.user.id.eq(userId);
    Pageable finalPageable = PageableUtils.prepareAuditSorting(pageable);
    return buildCreateOrderResponseDTO(filter, finalPageable);
  }

  private PageImpl<CreateOrderResponseDTO> buildCreateOrderResponseDTO(
      Predicate filter, Pageable finalPageable) {
    Page<Order> orderPage = orderRepository.findAll(filter, finalPageable);

    List<CreateOrderResponseDTO> orderResponseDTOList =
        orderPage.getContent().stream()
            .map(
                order -> {
                  CreateOrderResponseDTO responseDTO =
                      ModelMapperUtils.map(order, CreateOrderResponseDTO.class);
                  List<CreateOrderItemResponseDTO> orderItemResponseDTOS =
                      order.getOrderItems().stream()
                          .map(
                              orderItem -> {
                                CreateOrderItemResponseDTO itemResponseDTO =
                                    ModelMapperUtils.map(
                                        orderItem, CreateOrderItemResponseDTO.class);
                                itemResponseDTO.setProductId(orderItem.getProduct().getId());
                                return itemResponseDTO;
                              })
                          .collect(Collectors.toList());
                  responseDTO.setOrderItems(orderItemResponseDTOS);
                  return responseDTO;
                })
            .toList();

    return new PageImpl<>(
        orderResponseDTOList, orderPage.getPageable(), orderPage.getTotalElements());
  }

  public CreateOrderResponseDTO createOrdersVisitor(CreateOrderVisitorRequestDTO requestDTO) {
    User anonUser = userService.getUserByMail("anon@mail.com");

    Order order = ModelMapperUtils.map(requestDTO.getAddress(), Order.class);
    order = ModelMapperUtils.mapToExisting(requestDTO.getCart(), order);

    PaymentMethod paymentMethod =
        isNull(requestDTO.getTransactionID()) ? PAY_WITH_BANK_TRANSFER : CREDIT_CART;
    order.setPaymentMethod(paymentMethod);

    order.setOrderStatus(requestDTO.getOrderStatus());
    order.setUserEmail(requestDTO.getEmail());
    order.setUserName(requestDTO.getFirstName() + " " + requestDTO.getLastName());
    order.setUser(anonUser);

    Set<UUID> requiredProductIds =
        requestDTO.getCart().getItems().stream()
            .map(OrderSingleItemRequestDTO::getProductID)
            .collect(Collectors.toSet());
    Map<UUID, Product> productMap = productService.findByIdInAndMapByUUID(requiredProductIds);

    List<OrderItem> orderItems =
        requestDTO.getCart().getItems().stream()
            .map(
                oi -> {
                  if (!productMap.containsKey(oi.getProductID())) {
                    throw new BadRequestException("No product found with id: " + oi.getProductID());
                  }

                  OrderItem orderItem = ModelMapperUtils.map(oi, OrderItem.class);
                  Product product = productMap.get(oi.getProductID());
                  orderItem.setProduct(product);

                  return orderItem;
                })
            .toList();

    // Ensure orderItems is not null and not empty
    if (orderItems != null && !orderItems.isEmpty()) {
      for (OrderItem orderItem : orderItems) {
        orderItem.setOrder(order);
      }
      order.getOrderItems().addAll(orderItems);
    }
    setUniqueTraceCode(order);
    order.setIsInvoiceGenerated(false);

    Order savedOrder = orderRepository.saveAndFlush(order);

    // SEND USER AN E-MAIL
    try {
      User tempUser = new User();
      tempUser.setEmail(requestDTO.getEmail());
      tempUser.setFirstName(requestDTO.getFirstName());
      tempUser.setLastName(requestDTO.getLastName());
      mailService.sendHtmlEmail(tempUser, MailType.CREATE_ORDER, savedOrder);
    } catch (Exception e) {
      // log fail...
    }

    // UPDATE TRANSACTION
    if (nonNull(requestDTO.getTransactionID())) {
      TransactionRecord transactionRecord =
          transactionRecordService.findTransactionRecordByIdThrowsException(
              requestDTO.getTransactionID());
      transactionRecord.setTraceCode(savedOrder.getTraceCode());
      transactionRecordService.saveAndFlush(transactionRecord);
    }

    CreateOrderResponseDTO responseDTO =
        ModelMapperUtils.map(savedOrder, CreateOrderResponseDTO.class);
    List<CreateOrderItemResponseDTO> orderItemResponseDTOS =
        ModelMapperUtils.mapAll(savedOrder.getOrderItems(), CreateOrderItemResponseDTO.class);
    responseDTO.setOrderItems(orderItemResponseDTOS);

    handleOrderCreateAsyncEvents(savedOrder);
    return responseDTO;
  }

  private String beautifyTraceCode(String traceCode) {
    return traceCode.replaceAll("(.{3})(.{3})(.{3})", "$1-$2-$3");
  }

  public void createInvoiceRequest(UUID id) {
    Order order = findOrderByIdThrowsException(id);
    String filePath = beautifyTraceCode(order.getTraceCode()) + ".pdf";
    if (!s3InvoicePdfsService.doesObjectExist(filePath)) {
      handleOrderCreateAsyncEvents(order);
      return;
    }

    order.setIsInvoiceGenerated(true);
    orderRepository.saveAndFlush(order);
    // TODO: handle stuck between cases....
  }
}
