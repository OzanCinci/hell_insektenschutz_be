package com.ozan.be.transactions;

import com.ozan.be.common.BaseController;
import com.ozan.be.common.dtos.basic.BasicCreateResponseDTO;
import com.ozan.be.transactions.domain.TransactionRecordRequestDTO;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/transactions")
@AllArgsConstructor
@RestController
public class TransactionRecordController extends BaseController {
  private final TransactionRecordService transactionRecordService;

  @PostMapping
  public ResponseEntity<BasicCreateResponseDTO> createTransactionRecord(
      @Valid @RequestBody TransactionRecordRequestDTO requestDTO) {
    UUID id = transactionRecordService.createTransactionRecord(requestDTO);
    return ResponseEntity.ok(new BasicCreateResponseDTO(id));
  }
}
