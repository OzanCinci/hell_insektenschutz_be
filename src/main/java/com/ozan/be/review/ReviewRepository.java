package com.ozan.be.review;

import com.querydsl.core.types.Predicate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository
    extends JpaRepository<Review, UUID>, QuerydslPredicateExecutor<Review> {
  @Override
  @EntityGraph(attributePaths = {"product", "user"})
  Page<Review> findAll(Predicate filter, Pageable pageable);

  @EntityGraph(attributePaths = {"product"})
  Optional<Review> findById(UUID id);

  @Query(value = "SELECT product_id FROM review WHERE user_id = :userId", nativeQuery = true)
  Set<UUID> findProductIdsByUserId(UUID userId);

  boolean existsByUser_IdAndItemNameAndSecondaryName(
      UUID userId, String itemName, String secondaryName);
}
