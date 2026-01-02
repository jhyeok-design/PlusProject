package com.example.plusproject.domain.post.controller;


import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.post.model.request.PostCreateRequest;
import com.example.plusproject.domain.post.model.request.PostUpdateRequest;
import com.example.plusproject.domain.post.model.response.PostCreateResponse;
import com.example.plusproject.domain.post.model.response.PostReadResponse;
import com.example.plusproject.domain.post.model.response.PostUpdateResponse;
import com.example.plusproject.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    /*
     * 게시글 생성
     * */
    @PostMapping
    public ResponseEntity<CommonResponse<PostCreateResponse>> createPost(@AuthenticationPrincipal AuthUser authUser,  @Valid @RequestBody PostCreateRequest request) {

        PostCreateResponse result = postService.createPost(authUser, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("게시글 작성 완료", result));
    }

    /*
     * 게시글 단건 조회
     * */
    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse<PostReadResponse>> getPost(@PathVariable Long postId) {

        PostReadResponse result = postService.getPost(postId);

        return ResponseEntity.ok(CommonResponse.success("게시글 단건 조회 완료", result));
    }

    /*
     * 게시글 다건 조회
     * */
    @GetMapping
    public ResponseEntity<CommonResponse<Page<PostReadResponse>>> getPostList(Pageable pageable) {

        Page<PostReadResponse> result = postService.getPostList(pageable);

        return ResponseEntity.ok(CommonResponse.success("게시글 전체 조회 완료", result));
    }

    /*
     * 게시글 수정
     * */
    @PatchMapping("/{postId}")
    public ResponseEntity<CommonResponse<PostUpdateResponse>> updatePost(@PathVariable Long postId, @AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody PostUpdateRequest request) {

        PostUpdateResponse result = postService.updatePost(postId, authUser, request);

        return ResponseEntity.ok(CommonResponse.success("게시글 수정 완료", result));
    }

    /*
     * 게시글 삭제
     * */
    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponse<Void>> deletePost(@PathVariable Long postId, @AuthenticationPrincipal AuthUser authUser) {

        postService.deletePost(postId, authUser);

        return ResponseEntity.ok(CommonResponse.success("게시글 삭제 완료", null));
    }

}
