package com.example.plusproject.domain.review.repository;

import com.example.plusproject.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
