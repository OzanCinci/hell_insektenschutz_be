package com.ozan.be.management.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;

@Data
public class DiscountUpdateRequestDTO implements Serializable {
  @NotNull private Double percentage;
  @NotNull private Boolean active;
}
