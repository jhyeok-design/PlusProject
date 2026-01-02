package com.example.plusproject.domain.comment.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.domain.comment.entity.Comment;
import com.example.plusproject.domain.comment.model.CommentDto;
import com.example.plusproject.domain.comment.model.request.CommentCreateRequest;
import com.example.plusproject.domain.comment.model.request.CommentUpdateRequest;
import com.example.plusproject.domain.comment.model.response.CommentCreateResponse;
import com.example.plusproject.domain.comment.model.response.CommentReadResponse;
import com.example.plusproject.domain.comment.model.response.CommentUpdateResponse;
import com.example.plusproject.domain.comment.repository.CommentRepository;
import com.example.plusproject.domain.post.entity.Post;
import com.example.plusproject.domain.post.repository.PostRepository;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentCreateResponse createComment(Long userId, Long postId, CommentCreateRequest request) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_USER)
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_POST)
        );

        Comment comment = new Comment(request.getContent(), user, post);

        commentRepository.save(comment);

        CommentDto dto = CommentDto.from(comment);
        return CommentCreateResponse.from(dto);
    }
    @Transactional(readOnly = true)
    public Page<CommentReadResponse> getCommentList(Long postId, Pageable pageable) {

        Page<CommentDto> page = commentRepository.findCommentList(postId, pageable);
        return page
                .map(CommentReadResponse::from);
    }

    @Transactional
    public CommentUpdateResponse updateComment(Long commentId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_COMMENT)
        );

        comment.update(request.getContent());

        CommentDto dto = CommentDto.from(comment);

        return CommentUpdateResponse.from(dto);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new CustomException(ExceptionCode.NOT_FOUND_COMMENT);
        }
        commentRepository.deleteById(commentId);
    }
}
