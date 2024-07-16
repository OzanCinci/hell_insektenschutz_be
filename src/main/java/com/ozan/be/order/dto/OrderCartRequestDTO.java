package com.ozan.be.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class OrderCartRequestDTO implements Serializable {
  @NotNull private Integer numberOfItems;
  @NotNull private Double price;
  @NotNull private Double shippingPrice;
  @Valid @NotNull private List<OrderSingleItemRequestDTO> items;
}
