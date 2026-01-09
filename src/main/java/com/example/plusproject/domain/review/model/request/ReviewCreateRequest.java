package com.example.plusproject.domain.review.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewCreateRequest {

    @NotNull
    private Long productId;

    @NotBlank
    private String content;

    @NotNull
    private Integer score;
}
