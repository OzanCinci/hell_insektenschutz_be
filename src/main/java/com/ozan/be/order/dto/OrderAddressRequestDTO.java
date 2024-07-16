package com.ozan.be.order.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;

@Data
public class OrderAddressRequestDTO implements Serializable {
  @NotNull private String city;
  @NotNull private String country;
  @NotNull private String doorNumber;
  @NotNull private String postalCode;
  @NotNull private String state;
  @NotNull private String street;
}
