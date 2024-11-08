package com.ozan.be.order.dto;

import static java.util.Objects.isNull;

import com.ozan.be.order.domain.entity.Order;
import com.ozan.be.order.domain.entity.OrderItem;
import com.ozan.be.utils.ModelMapperUtils;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class OrderInvoiceDTO implements Serializable {
  final transient double TAX_FREE_MULTIPLIER = 1 / 1.19;
  private Customer customer;
  private OrderDetails order;

  @Data
  public static class Customer implements Serializable {
    private String fullName;
    private String address;
    private String postalCode;
    private String email;
  }

  @Data
  public static class OrderDetails implements Serializable {
    private String orderNumber;
    private String orderDate;
    private Double total;
    private Double shippingPrice;
    private Tax tax;
    private List<Item> items;
  }

  @Data
  public static class Tax implements Serializable {
    private Double percentage;
    private Double amount;
  }

  @Data
  public static class Item implements Serializable {
    private String title;
    private String description;
    private Integer quantity;
    private Double unitPrice;
  }

  public OrderInvoiceDTO(Order orderEntity) {
    this.customer = new Customer();
    this.customer.setFullName(orderEntity.getUserName());
    this.customer.setAddress(formatAddress(orderEntity));
    this.customer.setEmail(orderEntity.getUserEmail());

    this.order = new OrderDetails();
    this.order.setOrderNumber(
        orderEntity.getTraceCode().replaceAll("(.{3})(.{3})(.{3})", "$1-$2-$3"));
    this.order.setOrderDate(orderEntity.getCreatedAt().toString());
    this.order.setTotal(orderEntity.getPrice() + orderEntity.getShippingPrice());
    this.order.setShippingPrice(orderEntity.getShippingPrice());

    this.order.setTax(new Tax());
    this.order.getTax().setPercentage(19.0);
    this.order
        .getTax()
        .setAmount(orderEntity.getPrice() * 0.19); // TODO: Calculate correctly based on real tax.

    this.order.setItems(
        orderEntity.getOrderItems().stream()
            .map(this::mapOrderItemToItem)
            .collect(Collectors.toList()));
  }

  private Item mapOrderItemToItem(OrderItem orderItem) {
    Item item = new Item();
    item.setTitle(orderItem.getItemName() + " - " + orderItem.getSecondaryName());
    item.setDescription(formatAttributes(orderItem.getAttributes()));
    item.setQuantity(orderItem.getQuantity());
    item.setUnitPrice(orderItem.getPrice() * TAX_FREE_MULTIPLIER);
    return item;
  }

  private String formatAttributes(String stringifiedListAttributes) {
    List<String> attributesList =
        ModelMapperUtils.readFromStringToList(stringifiedListAttributes, String.class);
    if (isNull(attributesList)) {
      return null;
    }
    return String.join("\n", attributesList);
  }

  private String formatAddress(Order order) {
    StringBuilder address = new StringBuilder();
    if (order.getStreet() != null) address.append(order.getStreet()).append(", ");
    if (order.getDoorNumber() != null) address.append(order.getDoorNumber()).append(", ");
    if (order.getCity() != null) address.append(order.getCity()).append(", ");
    if (order.getState() != null) address.append(order.getState()).append(", ");
    if (order.getCountry() != null) address.append(order.getCountry());
    if (order.getPostalCode() != null)
      address.append(" (").append(order.getPostalCode().toString()).append(")");
    return address.toString();
  }
}
