package com.ozan.be.product.service;

import com.ozan.be.common.dtos.basic.BasicListResponseDTO;
import com.ozan.be.customException.types.DataNotFoundException;
import com.ozan.be.product.domain.PredefinedWindows;
import com.ozan.be.product.domain.dto.PredefinedWindowCsvDTO;
import com.ozan.be.product.domain.dto.PredefinedWindowDimensionResponseDTO;
import com.ozan.be.product.domain.projections.PredefinedWindowDimensionsDTO;
import com.ozan.be.product.repository.PredefinedWindowsRepository;
import com.ozan.be.utils.CsvReadUtils;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PredefinedWindowsService {

  private final PredefinedWindowsRepository predefinedWindowsRepository;

  @Transactional
  public List<PredefinedWindows> readFromCsvAndFillDB() {
    List<PredefinedWindowCsvDTO> predefinedWindowCsvDTOS =
        CsvReadUtils.readFromCsv(
            "/Users/ozan/Desktop/scripts/csv_parser/windows.csv",
            PredefinedWindowCsvDTO.class,
            row -> "Ungenormtes Dachfenster".equals(row.getWindowName()));

    Map<String, PredefinedWindows> uniquePredefinedWindowsMap =
        predefinedWindowCsvDTOS.stream()
            .map(PredefinedWindows::new)
            .collect(
                Collectors.toMap(
                    PredefinedWindows::getFullName,
                    Function.identity(),
                    (existing, replacement) -> existing));

    List<PredefinedWindows> allPredefinedWindows =
        new ArrayList<>(uniquePredefinedWindowsMap.values());
    List<PredefinedWindows> result = new ArrayList<>();
    int batchSize = 10;

    for (int i = 0; i < allPredefinedWindows.size(); i += batchSize) {
      List<PredefinedWindows> batch =
          allPredefinedWindows.subList(i, Math.min(i + batchSize, allPredefinedWindows.size()));
      List<PredefinedWindows> savedBatch = predefinedWindowsRepository.saveAll(batch);
      result.addAll(savedBatch);
    }

    return result;
  }

  public BasicListResponseDTO<String> getLevel1Options(String producer) {
    List<String> distinctWindowTypesByProducer =
        predefinedWindowsRepository.findDistinctWindowTypesByProducer(producer);
    return new BasicListResponseDTO<>(distinctWindowTypesByProducer);
  }

  public BasicListResponseDTO<String> getLevel2Options(String producer, String type) {
    List<String> distinctWindowNumbersByProducerAndType =
        predefinedWindowsRepository.findDistinctWindowNumbersByProducerAndType(producer, type);
    return new BasicListResponseDTO<>(distinctWindowNumbersByProducerAndType);
  }

  public PredefinedWindowDimensionResponseDTO getDimensions(
      String producer, String type, String number) {
    String processedNumber = number.replace("|::|", "/");
    PredefinedWindowDimensionsDTO dto =
        predefinedWindowsRepository
            .findDimensionsByProducerTypeAndNumber(producer, type, processedNumber)
            .orElseThrow(
                () ->
                    new DataNotFoundException(
                        String.format(
                            "No dimensions found for Predefined Window with Producer: '%s', Type: '%s', and Number: '%s'. Please ensure that the specified properties are correct.",
                            producer, type, processedNumber)));
    return new PredefinedWindowDimensionResponseDTO(dto);
  }
}
