package com.ozan.be.order;

import com.querydsl.core.types.Predicate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository
    extends JpaRepository<Order, UUID>, QuerydslPredicateExecutor<Order> {

  @Override
  @EntityGraph(attributePaths = {"orderItems"})
  Page<Order> findAll(Predicate filter, Pageable pageable);

  Optional<Order> findById(UUID id);

  boolean existsByTraceCode(String traceCode);

  Optional<Order> findByTraceCode(String traceCode);
}
