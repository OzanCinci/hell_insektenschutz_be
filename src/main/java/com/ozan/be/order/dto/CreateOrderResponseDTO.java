package com.ozan.be.order.dto;

import com.ozan.be.order.domain.OrderStatus;
import com.ozan.be.order.domain.PaymentMethod;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CreateOrderResponseDTO implements Serializable {
    private PaymentMethod paymentMethod;
    private Double shippingPrice;
    private Double price;
    private String address;
    private String traceCode;
    private String userEmail;
    private OrderStatus orderStatus;
    private List<CreateOrderItemResponseDTO> orderItems;
}