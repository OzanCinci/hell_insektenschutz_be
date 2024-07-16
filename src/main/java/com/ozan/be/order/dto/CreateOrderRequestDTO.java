package com.ozan.be.order.dto;

import jakarta.validation.Valid;
import java.io.Serializable;
import lombok.Data;

@Data
public class CreateOrderRequestDTO implements Serializable {
  @Valid private OrderAddressRequestDTO address;
  @Valid private OrderCreditCartRequestDTO creditCart;
  @Valid private OrderCartRequestDTO cart;
}
