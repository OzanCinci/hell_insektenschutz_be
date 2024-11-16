package com.ozan.be.review.dtos;

import com.ozan.be.product.domain.Product;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class ReviewResponseDTO {
  private UUID id;

  private Product product;

  private String userName; // user full name

  private String email;

  private Integer rating;

  private String comment;

  private Boolean approved;

  private Instant createdAt;

  private String itemName;

  private String secondaryName;
}
