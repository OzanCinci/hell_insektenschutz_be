package com.ozan.be.review.dtos;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class ReviewSummaryDTO implements Serializable {
  private UUID id;

  private String userName; // user full name

  private String email;

  private Integer rating;

  private String comment;

  private Boolean approved;

  private Instant createdAt;
}
