package com.ozan.be.order.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;

@Data
public class OrderCreditCartRequestDTO implements Serializable {
  @NotNull private String cvv;
  @NotNull private String expiration;
  @NotNull private String name;
  @NotNull private String number;
}
