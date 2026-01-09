package com.example.plusproject.domain.comment.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.AuthUser;
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

    /**
     * 댓글 생성
     */
    @Transactional
    public CommentCreateResponse createComment(Long postId, AuthUser authUser, CommentCreateRequest request) {

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_POST));

        Comment comment = new Comment(request.getContent(), user, post);

        commentRepository.save(comment);

        CommentDto dto = CommentDto.from(comment);

        return CommentCreateResponse.from(dto);
    }

    /**
     * 댓글 조회
     */
    @Transactional(readOnly = true)
    public Page<CommentReadResponse> readCommentList(Long postId, AuthUser authUser, Pageable pageable) {

        Long userId = authUser.getUserId();

        Page<CommentDto> page = commentRepository.findCommentList(postId, userId, pageable);
        return page
                .map(CommentReadResponse::from);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public CommentUpdateResponse updateComment(Long commentId, AuthUser authUser, CommentUpdateRequest request) {

        Comment comment = getCommentWithPermission(commentId, authUser.getUserId());

        comment.update(request.getContent());

        CommentDto dto = CommentDto.from(comment);

        return CommentUpdateResponse.from(dto);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteComment(Long commentId, AuthUser authUser) {

        Comment comment = getCommentWithPermission(commentId, authUser.getUserId());

        commentRepository.delete(comment);
    }

    /**
     * 권한 체크
     */
    private Comment getCommentWithPermission(Long commentId, Long userId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_COMMENT));

        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.NO_PERMISSION);
        }

        return comment;
    }
}
