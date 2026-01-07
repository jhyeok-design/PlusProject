package com.example.plusproject.domain.review.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewRankingDto {

    private final Long reviewId;
    private final Long viewCount;
}
