package com.ozan.be.utils;

import static java.util.Objects.isNull;

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

  static {
    modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    modelMapper.getConfiguration().setSkipNullEnabled(true);
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
}
