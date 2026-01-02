package com.example.plusproject.domain.post.model.response;

import com.example.plusproject.domain.comment.model.response.CommentReadResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PostDetailResponse {
    private final PostReadResponse post;
    private final List<CommentReadResponse> comments;

    public static PostDetailResponse from(PostReadResponse post, List<CommentReadResponse> comments) {
        return new PostDetailResponse(post, comments);
    }
}
