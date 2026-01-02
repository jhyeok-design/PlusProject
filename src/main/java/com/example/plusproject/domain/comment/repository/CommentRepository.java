package com.example.plusproject.domain.comment.repository;

import com.example.plusproject.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
    void deleteByPostId(Long postId);

}
