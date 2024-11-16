package com.ozan.be.product.repository;

import com.ozan.be.product.domain.PredefinedWindows;
import com.ozan.be.product.domain.projections.PredefinedWindowDimensionsDTO;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PredefinedWindowsRepository extends JpaRepository<PredefinedWindows, UUID> {

  @Query(
      value =
          "SELECT DISTINCT window_type FROM predefined_windows WHERE window_producer = :windowProducer ORDER BY window_type",
      nativeQuery = true)
  List<String> findDistinctWindowTypesByProducer(@Param("windowProducer") String windowProducer);

  @Query(
      value =
          "SELECT DISTINCT window_number FROM predefined_windows WHERE window_producer = :windowProducer AND window_type = :windowType ORDER BY window_number",
      nativeQuery = true)
  List<String> findDistinctWindowNumbersByProducerAndType(
      @Param("windowProducer") String windowProducer, @Param("windowType") String windowType);

  @Query(
      value =
          "SELECT window_width AS windowWidth, window_height AS windowHeight "
              + "FROM predefined_windows "
              + "WHERE window_producer = :windowProducer "
              + "AND window_type = :windowType "
              + "AND window_number = :windowNumber "
              + "LIMIT 1",
      nativeQuery = true)
  Optional<PredefinedWindowDimensionsDTO> findDimensionsByProducerTypeAndNumber(
      @Param("windowProducer") String windowProducer,
      @Param("windowType") String windowType,
      @Param("windowNumber") String windowNumber);
}
