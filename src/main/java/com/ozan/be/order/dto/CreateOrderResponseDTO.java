package com.ozan.be.order.dto;

import com.ozan.be.order.domain.enums.OrderStatus;
import com.ozan.be.order.domain.enums.PaymentMethod;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class CreateOrderResponseDTO implements Serializable {
  private UUID id;
  private String userEmail;
  private String userName;
  private PaymentMethod paymentMethod;
  private Double shippingPrice;
  private Double price;
  private String city;
  private String country;
  private String doorNumber;
  private Integer postalCode;
  private String street;
  private String state;
  private OrderStatus orderStatus;
  private String traceCode;
  private String cargoCode;
  private Integer numberOfItems;
  private Instant createdAt;
  private Instant updatedAt;
  private Boolean isInvoiceGenerated;
  private List<CreateOrderItemResponseDTO> orderItems;
}
