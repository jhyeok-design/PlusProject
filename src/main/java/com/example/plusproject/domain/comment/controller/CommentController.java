package com.example.plusproject.domain.comment.controller;

import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.comment.model.request.CommentCreateRequest;
import com.example.plusproject.domain.comment.model.request.CommentUpdateRequest;
import com.example.plusproject.domain.comment.model.response.CommentCreateResponse;
import com.example.plusproject.domain.comment.model.response.CommentReadResponse;
import com.example.plusproject.domain.comment.model.response.CommentUpdateResponse;
import com.example.plusproject.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommonResponse<CommentCreateResponse>> createComment(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long postId, @RequestBody CommentCreateRequest request) {
        CommentCreateResponse result = commentService.createComment(authUser, postId, request);

        return ResponseEntity.ok(CommonResponse.success("댓글 생성 완료", result));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommonResponse<Page<CommentReadResponse>>> readCommentList(@PathVariable Long postId, Pageable pageable) {
        Page<CommentReadResponse> result = commentService.readCommentList(postId, pageable);

        return ResponseEntity.ok(CommonResponse.success("댓글 전체 조회", result));
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommonResponse<CommentUpdateResponse>> updateComment(@PathVariable Long commentId, @AuthenticationPrincipal AuthUser authUser, @RequestBody CommentUpdateRequest request) {
        CommentUpdateResponse result = commentService.updateComment(commentId, authUser, request);

        return ResponseEntity.ok(CommonResponse.success("댓글 수정 완료", result));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CommonResponse<Void>> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal AuthUser authUser) {
        commentService.deleteComment(commentId, authUser);
        return ResponseEntity.ok(CommonResponse.success("댓글 삭제 완료", null));
    }

}
