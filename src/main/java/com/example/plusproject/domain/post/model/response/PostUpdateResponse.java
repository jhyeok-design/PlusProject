package com.example.plusproject.domain.post.model.response;

import com.example.plusproject.domain.post.model.PostDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PostUpdateResponse {

    private final Long id;
    private final Long userId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static PostUpdateResponse from(PostDto dto) {
        return new PostUpdateResponse(
                dto.getId(),
                dto.getUserId(),
                dto.getTitle(),
                dto.getContent(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }
}
