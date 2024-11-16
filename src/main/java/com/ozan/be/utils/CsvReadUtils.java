package com.ozan.be.utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvReadUtils {

  /**
   * Reads a CSV file and maps each row to an object of the specified class. Skips rows based on the
   * provided condition.
   *
   * @param file The CSV file to read.
   * @param clazz The class to map each row to.
   * @param skipCondition A predicate to determine if a row should be skipped.
   * @param <T> The type of the object.
   * @return A list of objects mapped from each row of the CSV file, excluding skipped rows.
   */
  public static <T> List<T> readFromCsv(File file, Class<T> clazz, Predicate<T> skipCondition) {
    List<T> result = new ArrayList<>();
    CsvMapper csvMapper = new CsvMapper();
    CsvSchema schema = csvMapper.schemaFor(clazz).withHeader().withColumnReordering(true);

    try (MappingIterator<T> iterator = csvMapper.readerFor(clazz).with(schema).readValues(file)) {
      while (iterator.hasNext()) {
        T row = iterator.next();
        // Skip processing this row if the skip condition is met
        if (skipCondition != null && skipCondition.test(row)) {
          log.info("Skipping row: {}", row);
          continue;
        }
        result.add(row);
      }
    } catch (IOException e) {
      log.error("Failed to read from CSV file {}: {}", file.getName(), e.getMessage(), e);
    }

    return result;
  }

  /**
   * Reads a CSV file and maps each row to an object of the specified class. Skips rows based on the
   * provided condition.
   *
   * @param filePath The path of the CSV file to read.
   * @param clazz The class to map each row to.
   * @param skipCondition A predicate to determine if a row should be skipped.
   * @param <T> The type of the object.
   * @return A list of objects mapped from each row of the CSV file, excluding skipped rows.
   */
  public static <T> List<T> readFromCsv(
      String filePath, Class<T> clazz, Predicate<T> skipCondition) {
    return readFromCsv(new File(filePath), clazz, skipCondition);
  }
}
