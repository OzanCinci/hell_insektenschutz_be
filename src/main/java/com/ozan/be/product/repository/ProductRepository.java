package com.ozan.be.product.repository;

import com.ozan.be.product.domain.Product;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
  Optional<Product> findById(UUID id);

  Optional<Product> findByCategoryAndName(String category, String name);

  List<Product> findByIdIn(Set<UUID> ids);
}
