package com.example.plusproject.domain.comment.controller;

import com.example.plusproject.domain.comment.model.request.CommentCreateRequest;
import com.example.plusproject.domain.comment.model.request.CommentUpdateRequest;
import com.example.plusproject.domain.comment.model.response.CommentCreateResponse;
import com.example.plusproject.domain.comment.model.response.CommentReadResponse;
import com.example.plusproject.domain.comment.model.response.CommentUpdateResponse;
import com.example.plusproject.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentCreateResponse> createComment(@RequestParam Long userId, @PathVariable Long postId, @RequestBody CommentCreateRequest request) {
        CommentCreateResponse result = commentService.createComment(userId, postId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Page<CommentReadResponse>> getCommentList(@PathVariable Long postId, Pageable pageable) {
        Page<CommentReadResponse> result = commentService.getCommentList(postId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest request) {
        CommentUpdateResponse result = commentService.updateComment(commentId, request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String>deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 완료");
    }

}
