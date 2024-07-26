package com.ozan.be.transactions;

import com.ozan.be.common.Auditable;
import com.ozan.be.transactions.domain.TransactionResource;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction_record")
public class TransactionRecord extends Auditable<UUID> implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String email;
  private Double amount;

  @Enumerated(EnumType.STRING)
  private TransactionResource transactionResource;

  // human readable 9 digit code for order tracing
  private String traceCode;
}
