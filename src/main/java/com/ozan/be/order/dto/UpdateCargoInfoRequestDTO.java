package com.ozan.be.order.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class UpdateCargoInfoRequestDTO implements Serializable {
  private String cargoCode;
}
