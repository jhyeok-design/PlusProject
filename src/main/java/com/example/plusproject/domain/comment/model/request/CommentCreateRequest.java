package com.example.plusproject.domain.comment.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    @NotBlank
    private String content;
}
