package com.example.plusproject.domain.post.controller;


import com.example.plusproject.domain.post.model.request.PostCreateRequest;
import com.example.plusproject.domain.post.model.request.PostUpdateRequest;
import com.example.plusproject.domain.post.model.response.PostCreateResponse;
import com.example.plusproject.domain.post.model.response.PostGetResponse;
import com.example.plusproject.domain.post.model.response.PostUpdateResponse;
import com.example.plusproject.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostCreateResponse> createPost(@RequestParam Long userId, @RequestBody PostCreateRequest request) {

        PostCreateResponse result = postService.createPost(userId, request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostGetResponse> getPost(@PathVariable Long postId) {

        PostGetResponse result = postService.getPost(postId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping
    public ResponseEntity<Page<PostGetResponse>> getPostList(Pageable pageable) {

        Page<PostGetResponse> result = postService.getPostList(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostUpdateResponse> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest request) {

        PostUpdateResponse result = postService.updatePost(postId, request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {

        postService.deletePost(postId);

        return ResponseEntity.status(HttpStatus.OK).body("게시글 삭제 완료");
    }

}
