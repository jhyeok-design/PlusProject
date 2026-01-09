package com.example.plusproject.domain.review.model.response;

import com.example.plusproject.domain.review.model.ReviewDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReviewCreateResponse {

    private final Long id;
    private final Long userId;
    private final Long productId;
    private final String content;
    private final Integer score;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ReviewCreateResponse from(ReviewDto reviewDto) {
        return new ReviewCreateResponse(
                reviewDto.getId(),
                reviewDto.getUser().getId(),
                reviewDto.getProduct().getId(),
                reviewDto.getContent(),
                reviewDto.getScore(),
                reviewDto.getCreatedAt(),
                reviewDto.getUpdatedAt()
        );
    }
}