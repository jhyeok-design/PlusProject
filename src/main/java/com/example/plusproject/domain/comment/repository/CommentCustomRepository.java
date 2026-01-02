package com.example.plusproject.domain.comment.repository;

import com.example.plusproject.domain.comment.model.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentCustomRepository {
    Page<CommentDto> findCommentList(Long postId, Long userId, Pageable pageable);
}
