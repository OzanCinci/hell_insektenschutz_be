package com.ozan.be.management.repository;

import com.ozan.be.management.domain.entity.Discount;
import com.querydsl.core.types.Predicate;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository
    extends JpaRepository<Discount, UUID>, QuerydslPredicateExecutor<Discount> {
  @Override
  @EntityGraph(attributePaths = {"user"})
  Iterable<Discount> findAll(Predicate filter);
}
