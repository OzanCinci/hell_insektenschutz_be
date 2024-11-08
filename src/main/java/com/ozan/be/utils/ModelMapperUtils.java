package com.ozan.be.utils;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@Slf4j
public class ModelMapperUtils {
  private static final ModelMapper modelMapper;
  private static final ObjectMapper objectMapper;

  static {
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    modelMapper.getConfiguration().setSkipNullEnabled(true);

    objectMapper = new ObjectMapper();
  }

  public static <D, T> D map(final T entity, Class<D> outClass) {
    try {
      return entity == null ? null : modelMapper.map(entity, outClass);
    } catch (Exception e) {
      log.error("Exception occurred at ModelMapperUtils map : " + e.getCause() + e.getMessage());
      return null;
    }
  }

  public static <S, D> D mapToExisting(S source, D destination) {
    try {
      modelMapper.map(source, destination);
      return destination;
    } catch (Exception e) {
      log.error(
          "Exception occurred at ModelMapperUtils mapToExisting: " + e.getCause() + e.getMessage());
      return null;
    }
  }

  public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass) {
    try {
      return entityList.isEmpty()
          ? Collections.emptyList()
          : entityList.stream().map(entity -> map(entity, outCLass)).toList();
    } catch (Exception e) {
      log.error("Exception occurred at ModelMapperUtils mapAll : " + e.getCause() + e.getMessage());
      return new ArrayList<>();
    }
  }

  public static <T> Map<UUID, T> convertListToMap(List<T> list, Function<T, UUID> id) {
    if (isNull(list) || list.isEmpty()) {
      return new HashMap<>();
    }
    return list.stream().collect(Collectors.toMap(id, Function.identity()));
  }

  public static <T, K> Map<K, T> convertListToArbitraryMap(
      List<T> list, Function<T, K> identifier) {
    if (isNull(list) || list.isEmpty()) {
      return new HashMap<>();
    }
    return list.stream()
        .collect(
            Collectors.toMap(identifier, Function.identity(), (existing, replacement) -> existing));
  }

  public static <T> String convertToJsonString(T object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("Failed to convert object to JSON: {}", e.getMessage(), e);
      return null;
    }
  }

  public static <T> T readFromString(String jsonString, Class<T> clazz) {
    try {
      return objectMapper.readValue(jsonString, clazz);
    } catch (JsonProcessingException e) {
      log.error("Failed to convert JSON string to object: {}", e.getMessage(), e);
      return null;
    }
  }

  public static <T> List<T> readFromStringToList(String stringifiedList, Class<T> clazz) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(
          stringifiedList,
          objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    } catch (JsonProcessingException e) {
      log.error("Failed to convert stringified List to List: {}", e.getMessage(), e);
      return null;
    }
  }
}
