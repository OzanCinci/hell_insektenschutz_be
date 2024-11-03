package com.ozan.be.order.domain;

import static java.util.Objects.nonNull;

import com.ozan.be.common.AbstractSearchFilter;
import com.ozan.be.order.QOrder;
import com.querydsl.core.types.Predicate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderSearchFilter extends AbstractSearchFilter {
  OrderStatus orderStatus;

  @Override
  public void buildPredicateList(List<Predicate> predicateList) {
    if (nonNull(orderStatus)) {
      predicateList.add(QOrder.order.orderStatus.eq(orderStatus));
    }
  }
}
