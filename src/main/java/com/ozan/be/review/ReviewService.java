package com.ozan.be.review;

import com.ozan.be.customException.types.BadRequestException;
import com.ozan.be.customException.types.DataNotFoundException;
import com.ozan.be.product.Product;
import com.ozan.be.product.ProductService;
import com.ozan.be.review.dtos.ReviewRequestDTO;
import com.ozan.be.review.dtos.ReviewResponseDTO;
import com.ozan.be.review.dtos.ReviewSummaryDTO;
import com.ozan.be.user.User;
import com.ozan.be.user.UserService;
import com.ozan.be.utils.ModelMapperUtils;
import com.ozan.be.utils.PageableUtils;
import com.querydsl.core.types.Predicate;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final ProductService productService;
  private final UserService userService;

  private Review getReviewByIdThrowsException(UUID id) {
    return reviewRepository
        .findById(id)
        .orElseThrow(() -> new DataNotFoundException("No review found with id: " + id));
  }

  public Page<ReviewResponseDTO> getAllReviews(Pageable pageable, Predicate filter) {
    Pageable finalPageable = PageableUtils.prepareAuditSorting(pageable);
    Page<Review> reviewPage = reviewRepository.findAll(filter, finalPageable);

    List<ReviewResponseDTO> reviewResponseDTOList =
        reviewPage.getContent().stream()
            .map(
                review -> {
                  User user = review.getUser();

                  ReviewResponseDTO responseDTO =
                      ModelMapperUtils.map(review, ReviewResponseDTO.class);
                  responseDTO.setEmail(user.getEmail());
                  responseDTO.setUserName(
                      user.getFirstName() + " " + user.getLastName().substring(0, 1) + ".");
                  return responseDTO;
                })
            .toList();

    return new PageImpl<>(
        reviewResponseDTOList, reviewPage.getPageable(), reviewPage.getTotalElements());
  }

  public Page<ReviewSummaryDTO> getAllReviewsWithoutProduct(Pageable pageable, Predicate filter) {
    Pageable finalPageable = PageableUtils.prepareAuditSorting(pageable);
    Page<Review> reviewPage = reviewRepository.findAll(filter, finalPageable);

    List<ReviewSummaryDTO> reviewResponseDTOList =
        reviewPage.getContent().stream()
            .map(
                review -> {
                  User user = review.getUser();

                  ReviewSummaryDTO responseDTO =
                      ModelMapperUtils.map(review, ReviewSummaryDTO.class);
                  responseDTO.setEmail(user.getEmail());
                  responseDTO.setUserName(
                      user.getFirstName() + " " + user.getLastName().substring(0, 1) + ".");
                  return responseDTO;
                })
            .toList();

    return new PageImpl<>(
        reviewResponseDTOList, reviewPage.getPageable(), reviewPage.getTotalElements());
  }

  @Transactional
  public void approveReview(UUID id) {
    Review review = getReviewByIdThrowsException(id);

    review.setApproved(true);
    reviewRepository.save(review);

    Product product = review.getProduct();

    Double newRating =
        (product.getRating() * product.getNumberOfRating() + review.getRating())
            / (product.getNumberOfRating() + 1);
    product.setRating(newRating);
    product.setNumberOfRating(product.getNumberOfRating() + 1);
    productService.saveAndFlush(product);
  }

  @Transactional
  public void deleteReview(UUID id) {
    Review review = getReviewByIdThrowsException(id);
    if (review.getApproved()) {
      throw new BadRequestException("Cannot delete approved review.");
    }

    reviewRepository.delete(review);
  }

  @Transactional
  public UUID createReview(ReviewRequestDTO requestDTO, UUID userId) {
    User user = userService.getUserById(userId);

    validateNoMultipleReviewForSameProduct(userId, requestDTO);

    Product product = productService.getProductByIdThrowsException(requestDTO.getProductId());

    Review review = ModelMapperUtils.map(requestDTO, Review.class);
    review.setProduct(product);
    review.setUser(user);
    review.setApproved(false);

    Review savedReview = reviewRepository.saveAndFlush(review);
    return savedReview.getId();
  }

  private void validateNoMultipleReviewForSameProduct(UUID userId, ReviewRequestDTO requestDTO) {
    Boolean exists =
        reviewRepository.existsByUser_IdAndItemNameAndSecondaryName(
            userId, requestDTO.getItemName(), requestDTO.getSecondaryName());
    if (exists) {
      throw new BadRequestException("You have already made a review on this product.");
    }
  }

  public Page<ReviewSummaryDTO> getReviewsByUserId(Pageable pageable, UUID userId) {
    Predicate filter = QReview.review.user.id.eq(userId);
    return getAllReviewsWithoutProduct(pageable, filter);
  }
}
