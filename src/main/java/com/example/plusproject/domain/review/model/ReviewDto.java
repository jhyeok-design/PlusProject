package com.example.plusproject.domain.review.model;

import com.example.plusproject.domain.product.entity.Product;
import com.example.plusproject.domain.review.entity.Review;
import com.example.plusproject.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReviewDto {

    private final Long id;
    private final User user;
    private final Product product;
    private final String content;
    private final Integer score;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ReviewDto from(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getUser(),
                review.getProduct(),
                review.getContent(),
                review.getScore(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
