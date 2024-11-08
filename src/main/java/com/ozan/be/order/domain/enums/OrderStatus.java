package com.ozan.be.order.domain.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public enum OrderStatus {
  PENDING_PAYMENT,
  ACTIVE,
  IN_SHIPPING,
  DELIVERED;

  public static List<OrderStatus> getSortedOrderStatusNames() {
    return Arrays.stream(OrderStatus.values())
        .sorted(Comparator.comparing(OrderStatus::ordinal))
        .toList();
  }
}
