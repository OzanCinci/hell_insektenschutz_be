package com.ozan.be.order.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class CreateOrderItemResponseDTO implements Serializable {
  private String itemName;
  private String secondaryName;
  private String attributes;
  private String cartImage;
  private Integer quantity;
  private Double price;
}
