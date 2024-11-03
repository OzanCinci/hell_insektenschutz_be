package com.ozan.be.review.domain;

import static java.util.Objects.nonNull;

import com.ozan.be.common.AbstractSearchFilter;
import com.ozan.be.review.QReview;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.UUID;
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
public class ReviewSearchFilter extends AbstractSearchFilter {
  Boolean approved;
  UUID productId;

  @Override
  public void buildPredicateList(List<Predicate> predicateList) {
    if (nonNull(approved)) {
      predicateList.add(QReview.review.approved.eq(approved));
    }

    if (nonNull(productId)) {
      predicateList.add(QReview.review.product.id.eq(productId));
    }
  }
}
