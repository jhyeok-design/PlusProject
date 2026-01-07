package com.example.plusproject.domain.post.controller;


import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.post.model.request.PostCreateRequest;
import com.example.plusproject.domain.post.model.request.PostUpdateRequest;
import com.example.plusproject.domain.post.model.response.PostCreateResponse;
import com.example.plusproject.domain.post.model.response.PostDetailResponse;
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
    public ResponseEntity<CommonResponse<PostCreateResponse>> createPost(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody PostCreateRequest request) {

        PostCreateResponse result = postService.createPost(authUser, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("게시글 작성 완료", result));
    }

    /*
     * 게시글 단건 조회
     * */
    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse<PostDetailResponse>> readPost(@PathVariable Long postId) {

        PostDetailResponse result = postService.readPost(postId);

        return ResponseEntity.ok(CommonResponse.success("게시글 단건 조회 완료", result));
    }

    /*
     * 게시글 다건 조회
     * */
    @GetMapping
    public ResponseEntity<CommonResponse<Page<PostReadResponse>>> readPostList(Pageable pageable) {

        Page<PostReadResponse> result = postService.readPostList(pageable);

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

    /*
     * 게시글 검색 (제목 키워드 및 유저 닉네임 검색)
     * */
    @GetMapping("/search")
    public ResponseEntity<CommonResponse<Page<PostReadResponse>>> searchPostPage(@RequestParam(required = false) String keyword, @RequestParam(required = false) String nickname, Pageable pageable) {
        Page<PostReadResponse> result = postService.searchPostPage(keyword, nickname, pageable);

        return ResponseEntity.ok(CommonResponse.success("게시글 검색 성공", result));
    }

    //    /*
//     * 검색기능 v2 cache적용
//     * */
//    @GetMapping("/searchV2")
//    public ResponseEntity<CommonResponse<Page<PostReadResponse>>> searchPostPageV2(@RequestParam(required = false) String keyword, @RequestParam(required = false) String nickname, Pageable pageable) {
//        Page<PostReadResponse> result = postService.searchPostPageV2(keyword, nickname, pageable);
//
//        return ResponseEntity.ok(CommonResponse.success("게시글 검색 성공", result));
//    }
    /*
     * 검색기능 v3 redis적용
     * */
    @GetMapping("/searchV3")
    public ResponseEntity<CommonResponse<Page<PostReadResponse>>> searchPostPageV3(@RequestParam(required = false) String keyword, @RequestParam(required = false) String nickname, Pageable pageable) {
        Page<PostReadResponse> result = postService.searchPostPageV3(keyword, nickname, pageable);

        return ResponseEntity.ok(CommonResponse.success("게시글 검색 성공", result));
    }


}
