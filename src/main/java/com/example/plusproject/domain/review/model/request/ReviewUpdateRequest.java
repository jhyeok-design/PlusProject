package com.example.plusproject.domain.review.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequest {

    private String content;
    private Integer score;
}