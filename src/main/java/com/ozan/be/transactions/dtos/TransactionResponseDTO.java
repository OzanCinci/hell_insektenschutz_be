package com.ozan.be.transactions.dtos;

import com.ozan.be.transactions.domain.TransactionResource;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class TransactionResponseDTO implements Serializable {
  private UUID id;
  private String email;
  private Double amount;
  private TransactionResource transactionResource;
  private String traceCode;
  private Instant createdAt;
}
