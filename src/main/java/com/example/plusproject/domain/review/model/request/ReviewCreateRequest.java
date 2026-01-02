package com.example.plusproject.domain.review.model.request;


import lombok.Getter;

@Getter
public class ReviewCreateRequest {

    private Long userId;
    private Long productId;
    private String content;
    private Integer score;
}
