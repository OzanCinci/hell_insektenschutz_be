package com.ozan.be.transactions;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionResourceRepository extends JpaRepository<TransactionRecord, UUID> {
  Optional<TransactionRecord> findById(UUID id);

  Page<TransactionRecord> findAll(Pageable pageable);
}
