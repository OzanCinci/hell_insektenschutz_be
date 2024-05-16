package com.ozan.be.order.dtos;

import com.ozan.be.order.domain.OrderStatus;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class OrderResponseDTO implements Serializable {
  private UUID id;
  private String userName;
  private String userEmail;
  private String paymentMethod;
  private Double shippingPrice;
  private Double totalPrice;
  private String address;
  private String city;
  private Integer postalCode;
  private String country;
  private OrderStatus orderStatus;
  private List<OrderItemResponseDTO> orderItems;
}
