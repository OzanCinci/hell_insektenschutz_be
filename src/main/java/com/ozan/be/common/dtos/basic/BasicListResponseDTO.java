package com.ozan.be.common.dtos.basic;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class BasicListResponseDTO<T> implements Serializable {
  private List<T> data;
}
