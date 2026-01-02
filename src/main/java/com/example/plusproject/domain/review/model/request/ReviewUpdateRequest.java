package com.example.plusproject.domain.review.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewUpdateRequest {

    @NotBlank
    private String content;

    @NotNull
    private Integer score;
}
