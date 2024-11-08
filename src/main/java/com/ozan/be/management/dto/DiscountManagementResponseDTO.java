package com.ozan.be.management.dto;

import com.ozan.be.management.domain.enums.DiscountProductType;
import com.ozan.be.management.domain.enums.DiscountType;
import com.ozan.be.user.dtos.UserResponseDTO;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class DiscountManagementResponseDTO implements Serializable {
  private UUID id;
  private Double percentage;
  private DiscountType discountType;
  private DiscountProductType discountProductType;
  private Instant validUntil;
  private boolean isActive;
  private UserResponseDTO user;
}
