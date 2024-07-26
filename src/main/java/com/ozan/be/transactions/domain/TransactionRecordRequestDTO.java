package com.ozan.be.transactions.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;

@Data
public class TransactionRecordRequestDTO implements Serializable {
  @NotNull private String email;
  @NotNull private Double amount;

  @NotNull
  @Enumerated(EnumType.STRING)
  private TransactionResource transactionResource;
}
