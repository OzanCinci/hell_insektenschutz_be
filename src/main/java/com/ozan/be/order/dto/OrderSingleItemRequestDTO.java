package com.ozan.be.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
public class OrderSingleItemRequestDTO implements Serializable {
  @NotNull private UUID productID;

  @NotNull
  @NotEmpty
  @Size(max = 2048)
  private String attributes;

  @NotNull @NotEmpty private String cartImage;
  @NotNull @NotEmpty private String itemName;
  @NotNull @NotEmpty private String secondaryName;
  @NotNull private Double price;
  @NotNull private Integer quantity;
}
