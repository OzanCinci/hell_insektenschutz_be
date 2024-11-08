package com.ozan.be.management.controller;

import com.ozan.be.common.BaseController;
import com.ozan.be.common.dtos.basic.BasicReponseDTO;
import com.ozan.be.review.ReviewService;
import com.ozan.be.review.domain.ReviewSearchFilter;
import com.ozan.be.review.dtos.ReviewResponseDTO;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/management/reviews")
@AllArgsConstructor
@RestController
public class ManagementReviewController extends BaseController {
  private final ReviewService reviewService;

  @GetMapping
  public ResponseEntity<Page<ReviewResponseDTO>> getAllReviews(
      @PageableDefault(size = 10) Pageable pageable,
      @ParameterObject ReviewSearchFilter reviewSearchFilter) {
    Page<ReviewResponseDTO> response =
        reviewService.getAllReviews(pageable, reviewSearchFilter.getPredicate());
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<BasicReponseDTO> approveReview(@PathVariable("id") UUID id) {
    reviewService.approveReview(id);
    return ResponseEntity.ok(new BasicReponseDTO(true));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<BasicReponseDTO> deleteReview(@PathVariable("id") UUID id) {
    reviewService.deleteReview(id);
    return ResponseEntity.ok(new BasicReponseDTO(true));
  }
}
