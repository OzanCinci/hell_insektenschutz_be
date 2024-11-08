package com.ozan.be.common.dtos.basic;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicCreateResponseDTO implements Serializable {
  private UUID id;
}
