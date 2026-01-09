package com.example.plusproject.domain.comment.model.response;

import com.example.plusproject.domain.comment.model.CommentDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CommentCreateResponse {

    private final Long id;
    private final Long userId;
    private final Long postId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CommentCreateResponse from(CommentDto dto) {
        return new CommentCreateResponse(
                dto.getId(),
                dto.getUserId(),
                dto.getPostId(),
                dto.getContent(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }
}

