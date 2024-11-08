package com.ozan.be.management.controller;

import com.ozan.be.transactions.TransactionRecordService;
import com.ozan.be.transactions.dtos.TransactionResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/management/transactions")
@AllArgsConstructor
@RestController
public class ManagementTransactionController {
  private final TransactionRecordService transactionRecordService;

  @GetMapping
  public ResponseEntity<Page<TransactionResponseDTO>> getAllTransactions(
      @PageableDefault(size = 20) Pageable pageable) {
    Page<TransactionResponseDTO> responseDTOS =
        transactionRecordService.getAllTransactions(pageable);
    return ResponseEntity.ok(responseDTOS);
  }
}
