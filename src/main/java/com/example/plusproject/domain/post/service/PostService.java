package com.example.plusproject.domain.post.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.domain.comment.model.response.CommentReadResponse;
import com.example.plusproject.domain.comment.repository.CommentRepository;
import com.example.plusproject.domain.post.entity.Post;
import com.example.plusproject.domain.post.model.PostDto;
import com.example.plusproject.domain.post.model.request.PostCreateRequest;
import com.example.plusproject.domain.post.model.request.PostUpdateRequest;
import com.example.plusproject.domain.post.model.response.PostCreateResponse;
import com.example.plusproject.domain.post.model.response.PostDetailResponse;
import com.example.plusproject.domain.post.model.response.PostReadResponse;
import com.example.plusproject.domain.post.model.response.PostUpdateResponse;
import com.example.plusproject.domain.post.repository.PostRepository;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostCreateResponse createPost(AuthUser authUser, PostCreateRequest request) {

        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_USER)
        );

        Post post = new Post(request.getTitle(), request.getContent(), user);

        postRepository.save(post);

        PostDto dto = PostDto.from(post);

        return PostCreateResponse.from(dto);
    }

    @Transactional(readOnly = true)
    public PostDetailResponse readPost(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_POST)
        );

        List<CommentReadResponse> comments = commentRepository
                .findAllByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(CommentReadResponse::from)
                .toList();

        PostReadResponse dto = PostReadResponse.from(PostDto.from(post));

        return PostDetailResponse.from(dto, comments);

    }

    @Transactional(readOnly = true)
    public Page<PostReadResponse> readPostList(Pageable pageable) {

        Page<PostDto> page = postRepository.findPostList(pageable);

        return page.map(PostReadResponse::from);
    }

    @Transactional
    public PostUpdateResponse updatePost(Long postId, AuthUser authUser, PostUpdateRequest request) {

        Post post = getPostWithPermission(postId, authUser.getUserId());

        post.update(
                request.getTitle(),
                request.getContent()
        );

        PostDto dto = PostDto.from(post);
        return PostUpdateResponse.from(dto);
    }

    @Transactional
    public void deletePost(Long postId, AuthUser authUser) {

        Post post = getPostWithPermission(postId, authUser.getUserId());

        commentRepository.deleteByPostId(postId);
        postRepository.delete(post);
    }


    private Post getPostWithPermission(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_POST)
        );


        if (!post.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.NO_PERMISSION);
        }

        return post;
    }
}
