package com.ozan.be.review;

import com.ozan.be.common.BaseController;
import com.ozan.be.common.dtos.BasicCreateResponseDTO;
import com.ozan.be.review.domain.ReviewSearchFilter;
import com.ozan.be.review.dtos.ReviewRequestDTO;
import com.ozan.be.review.dtos.ReviewSummaryDTO;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/review")
@AllArgsConstructor
@RestController
public class ReviewController extends BaseController {
  private final ReviewService reviewService;

  @PostMapping
  public ResponseEntity<BasicCreateResponseDTO> createReview(
      @Valid @RequestBody ReviewRequestDTO requestDTO) {
    UUID userId = getCurrentUser().getId();
    UUID id = reviewService.createReview(requestDTO, userId);
    return ResponseEntity.ok(new BasicCreateResponseDTO(id));
  }

  @GetMapping
  public ResponseEntity<Page<ReviewSummaryDTO>> getReviewsByProductId(
      @PageableDefault(size = 5) Pageable pageable,
      @ParameterObject ReviewSearchFilter reviewSearchFilter) {
    Page<ReviewSummaryDTO> response =
        reviewService.getAllReviewsWithoutProduct(pageable, reviewSearchFilter.getPredicate());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/me")
  public ResponseEntity<Page<ReviewSummaryDTO>> getReviewsByUserId(
      @PageableDefault(size = 5) Pageable pageable) {
    UUID userId = getCurrentUser().getId();
    Page<ReviewSummaryDTO> response = reviewService.getReviewsByUserId(pageable, userId);
    return ResponseEntity.ok(response);
  }
}
