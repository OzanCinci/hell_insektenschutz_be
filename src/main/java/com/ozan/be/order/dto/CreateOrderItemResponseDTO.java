package com.ozan.be.order.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateOrderItemResponseDTO implements Serializable {
    private String itemName;
    private String secondaryName;
    private String attributes;
    private String cartImage;
    private Integer quantity;
    private Double price;
}
