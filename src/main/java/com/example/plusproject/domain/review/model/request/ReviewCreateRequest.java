package com.example.plusproject.domain.review.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewCreateRequest {

    @NotNull
    private Long productId;

    @NotBlank
    private String content;

    @NotNull
    private Integer score;
}
