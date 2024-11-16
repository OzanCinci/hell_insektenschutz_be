package com.ozan.be.order.dtos;

import com.ozan.be.product.domain.Product;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
public class OrderItemResponseDTO implements Serializable {
  private UUID id;
  private Integer quantity;
  private Double price;
  private String measurements;
  private Product product;
}
