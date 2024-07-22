package com.ozan.be.review.dtos;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
public class ReviewRequestDTO implements Serializable {
  @NotNull private UUID productId;
  @NotNull private Integer rating;
  @NotNull private String comment;

  @NotNull private String reviewImage;
  @NotNull private String itemName;
  @NotNull private String secondaryName;
}
