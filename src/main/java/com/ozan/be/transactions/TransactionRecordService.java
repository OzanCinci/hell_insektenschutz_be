package com.ozan.be.transactions;

import static java.util.Objects.isNull;

import com.ozan.be.customException.types.BadRequestException;
import com.ozan.be.customException.types.DataNotFoundException;
import com.ozan.be.transactions.domain.TransactionRecordRequestDTO;
import com.ozan.be.transactions.dtos.TransactionResponseDTO;
import com.ozan.be.utils.ModelMapperUtils;
import com.ozan.be.utils.PageableUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransactionRecordService {
  private final TransactionResourceRepository transactionResourceRepository;

  @Transactional
  public UUID createTransactionRecord(TransactionRecordRequestDTO requestDTO) {
    TransactionRecord transactionRecord = ModelMapperUtils.map(requestDTO, TransactionRecord.class);
    if (isNull(transactionRecord)) {
      throw new BadRequestException("Cannot map transaction record.");
    }
    TransactionRecord saved = transactionResourceRepository.saveAndFlush(transactionRecord);
    return saved.getId();
  }

  public TransactionRecord findTransactionRecordByIdThrowsException(UUID id) {
    return transactionResourceRepository
        .findById(id)
        .orElseThrow(() -> new DataNotFoundException("Couldn't find transaction with id: " + id));
  }

  @Transactional
  public void saveAndFlush(TransactionRecord transactionRecord) {
    transactionResourceRepository.saveAndFlush(transactionRecord);
  }

  public Page<TransactionResponseDTO> getAllTransactions(Pageable pageable) {
    Pageable finalPageable = PageableUtils.prepareAuditSorting(pageable);
    Page<TransactionRecord> transactionRecordPage =
        transactionResourceRepository.findAll(finalPageable);
    List<TransactionResponseDTO> transactionResponseDTOList =
        ModelMapperUtils.mapAll(transactionRecordPage.getContent(), TransactionResponseDTO.class);
    return new PageImpl<>(
        transactionResponseDTOList,
        transactionRecordPage.getPageable(),
        transactionRecordPage.getTotalElements());
  }
}
