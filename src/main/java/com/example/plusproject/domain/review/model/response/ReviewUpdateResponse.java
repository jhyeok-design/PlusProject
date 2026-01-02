package com.example.plusproject.domain.review.model.response;

import com.example.plusproject.domain.review.model.ReviewDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReviewUpdateResponse {

    private final Long id;
    private final String content;
    private final Integer score;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ReviewUpdateResponse from(ReviewDto reviewDto) {

        return new ReviewUpdateResponse(
                reviewDto.getId(),
                reviewDto.getContent(),
                reviewDto.getScore(),
                reviewDto.getCreatedAt(),
                reviewDto.getUpdatedAt()
        );
    }
}
