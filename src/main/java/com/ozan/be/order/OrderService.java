package com.ozan.be.order;

import com.ozan.be.customException.types.BadRequestException;
import com.ozan.be.customException.types.DataNotFoundException;
import com.ozan.be.order.domain.OrderStatus;
import com.ozan.be.order.dtos.OrderCreateRequestDTO;
import com.ozan.be.order.dtos.OrderItemRequestDTO;
import com.ozan.be.order.dtos.OrderItemResponseDTO;
import com.ozan.be.order.dtos.OrderResponseDTO;
import com.ozan.be.product.Product;
import com.ozan.be.product.ProductService;
import com.ozan.be.user.User;
import com.ozan.be.user.UserService;
import com.ozan.be.utils.ModelMapperUtils;
import com.ozan.be.utils.PageableUtils;
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

  public Page<OrderResponseDTO> getAllOrders(Pageable pageable, Predicate filter) {
    Pageable finalPageable = PageableUtils.prepareAuditSorting(pageable);
    Page<Order> orderPage = orderRepository.findAll(filter, finalPageable);

    List<OrderResponseDTO> orderResponseDTOList =
        orderPage.getContent().stream()
            .map(
                order -> {
                  OrderResponseDTO responseDTO =
                      ModelMapperUtils.map(order, OrderResponseDTO.class);
                  List<OrderItemResponseDTO> orderItemResponseDTOList =
                      ModelMapperUtils.mapAll(order.getOrderItems(), OrderItemResponseDTO.class);
                  responseDTO.setOrderItems(orderItemResponseDTOList);
                  return responseDTO;
                })
            .toList();

    return new PageImpl<>(
        orderResponseDTOList, orderPage.getPageable(), orderPage.getTotalElements());
  }

  private Order findOrderByIdThrowsException(UUID id) {
    return orderRepository
        .findById(id)
        .orElseThrow(() -> new DataNotFoundException("No order found with id: " + id));
  }

  public void updateOrderStatus(UUID id, OrderStatus orderStatus) {
    Order order = findOrderByIdThrowsException(id);

    validateOrderStatusChange(order.getOrderStatus(), orderStatus);

    order.setOrderStatus(orderStatus);
    orderRepository.saveAndFlush(order);
  }

  private void validateOrderStatusChange(OrderStatus orderStatus, OrderStatus newOrderStatus) {
    List<OrderStatus> orderedStatus = OrderStatus.getSortedOrderStatusNames();
    if (orderedStatus.indexOf(orderStatus) >= orderedStatus.indexOf(newOrderStatus)) {
      throw new BadRequestException("Invalid order status request for the current order.");
    }
  }

  @Transactional
  public void createOrder(UUID userId, OrderCreateRequestDTO requestDTO) {
    User user = userService.getUserById(userId);

    Order order = ModelMapperUtils.map(requestDTO, Order.class);
    order.setUserEmail(user.getEmail());
    order.setUserName(user.getFirstName() + " " + user.getLastName());
    order.setUser(user);

    Set<UUID> requiredProductIds =
        requestDTO.getOrderItemsList().stream()
            .map(OrderItemRequestDTO::getProductId)
            .collect(Collectors.toSet());

    Map<UUID, Product> productMap = productService.findByIdInAndMapByUUID(requiredProductIds);

    List<OrderItem> orderItems =
        requestDTO.getOrderItemsList().stream()
            .map(
                oi -> {
                  if (!productMap.containsKey(oi.getProductId())) {
                    throw new BadRequestException("No product found with id: " + oi.getProductId());
                  }

                  OrderItem orderItem = ModelMapperUtils.map(oi, OrderItem.class);
                  orderItem.setOrder(order);
                  Product product = productMap.get(oi.getProductId());
                  orderItem.setProduct(product);

                  return orderItem;
                })
            .toList();

    order.getOrderItems().addAll(orderItems);
    orderRepository.saveAndFlush(order);
  }
}
