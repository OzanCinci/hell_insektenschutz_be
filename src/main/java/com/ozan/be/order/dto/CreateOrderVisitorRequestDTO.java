package com.ozan.be.order.dto;

import com.ozan.be.order.domain.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
public class CreateOrderVisitorRequestDTO implements Serializable {
  @Valid private OrderAddressRequestDTO address;
  @Valid private OrderCartRequestDTO cart;
  private UUID transactionID;
  private OrderStatus orderStatus;
  @NotNull private String email;
  @NotNull private String firstName;
  @NotNull private String lastName;
  private String telephone;
}
