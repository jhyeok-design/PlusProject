package com.example.plusproject.domain.review.model.response;

import com.example.plusproject.domain.review.model.ReviewDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReviewReadResponse {

    private final Long id;
    private final Long userId;
    private final String userNickname;
    private final Long productId;
    private final String productName;
    private final String content;
    private final Integer score;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ReviewReadResponse from(ReviewDto reviewDto) {

        return new ReviewReadResponse(
                reviewDto.getId(),
                reviewDto.getUser().getId(),
                reviewDto.getUser().getNickname(),
                reviewDto.getProduct().getId(),
                reviewDto.getProduct().getName(),
                reviewDto.getContent(),
                reviewDto.getScore(),
                reviewDto.getCreatedAt(),
                reviewDto.getUpdatedAt()
        );
    }
}
