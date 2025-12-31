package com.example.plusproject.domain.reivew.repository;

import com.example.plusproject.domain.reivew.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
