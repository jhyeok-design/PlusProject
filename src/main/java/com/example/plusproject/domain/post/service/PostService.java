package com.example.plusproject.domain.post.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.domain.comment.repository.CommentRepository;
import com.example.plusproject.domain.post.entity.Post;
import com.example.plusproject.domain.post.model.PostDto;
import com.example.plusproject.domain.post.model.request.PostCreateRequest;
import com.example.plusproject.domain.post.model.request.PostUpdateRequest;
import com.example.plusproject.domain.post.model.response.PostCreateResponse;
import com.example.plusproject.domain.post.model.response.PostGetResponse;
import com.example.plusproject.domain.post.model.response.PostUpdateResponse;
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
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostCreateResponse createPost(Long userId, PostCreateRequest request) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_USER)
        );

        Post post = new Post(request.getTitle(), request.getContent(), user);

        postRepository.save(post);

        PostDto dto = PostDto.from(post);

        return PostCreateResponse.from(dto);
    }

    @Transactional(readOnly = true)
    public PostGetResponse getPost(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_POST)
        );

        PostDto dto = PostDto.from(post);

        return PostGetResponse.from(dto);

    }

    @Transactional(readOnly = true)
    public Page<PostGetResponse> getPostList(Pageable pageable) {

        Page<PostDto> page = postRepository.findPostList(pageable);

        return page.map(PostGetResponse::from);
    }

    @Transactional
    public PostUpdateResponse updatePost(Long postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_POST)
        );

        post.update(
                request.getTitle(),
                request.getContent()
        );

        PostDto dto = PostDto.from(post);
        return PostUpdateResponse.from(dto);
    }

    @Transactional
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new CustomException(ExceptionCode.NOT_FOUND_POST);
        }

        postRepository.deleteById(postId);
    }
}
