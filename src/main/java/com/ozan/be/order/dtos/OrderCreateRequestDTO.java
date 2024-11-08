package com.ozan.be.order.dtos;

import com.ozan.be.order.domain.enums.OrderStatus;
import com.ozan.be.order.domain.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class OrderCreateRequestDTO implements Serializable {
  @NotNull private PaymentMethod paymentMethod;

  @NotNull private Double shippingPrice;

  @NotNull private Double totalPrice;

  @NotNull private String address;

  @NotNull private String city;

  @NotNull private Integer postalCode;

  @NotNull private String country;

  @NotNull private OrderStatus orderStatus;

  @Valid private List<OrderItemRequestDTO> orderItemsList;
}
