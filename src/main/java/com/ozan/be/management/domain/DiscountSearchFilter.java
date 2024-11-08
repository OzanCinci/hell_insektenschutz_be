package com.ozan.be.management.domain;

import static java.util.Objects.nonNull;

import com.ozan.be.common.AbstractSearchFilter;
import com.ozan.be.management.domain.entity.QDiscount;
import com.ozan.be.management.domain.enums.DiscountType;
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
public class DiscountSearchFilter extends AbstractSearchFilter {
  DiscountType discountType;
  Boolean isActive;

  @Override
  public void buildPredicateList(List<Predicate> predicateList) {
    if (nonNull(discountType)) {
      predicateList.add(QDiscount.discount.discountType.eq(discountType));
    }

    if (nonNull(isActive)) {
      predicateList.add(QDiscount.discount.isActive.eq(isActive));
    }
  }
}
