package com.ozan.be.user.repository;

import com.ozan.be.user.User;
import com.querydsl.core.types.Predicate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, QuerydslPredicateExecutor<User> {

  Optional<User> findByEmail(String email);

  Optional<User> findById(UUID id);

  @Override
  Page<User> findAll(Predicate filter, Pageable pageable);
}
