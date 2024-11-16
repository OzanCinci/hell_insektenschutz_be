package com.ozan.be.common.dtos.basic;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicListResponseDTO<T> implements Serializable {
  private List<T> data;
}
