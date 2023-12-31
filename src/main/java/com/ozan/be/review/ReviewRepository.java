package com.ozan.be.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    public List<Review> findAllByApproved(Boolean approved);

    public Optional<Review> findReviewById(Integer reviewID);
}
