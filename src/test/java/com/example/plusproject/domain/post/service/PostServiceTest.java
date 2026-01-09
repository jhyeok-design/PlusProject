package com.example.plusproject.domain.post.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.enums.UserRole;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.domain.comment.entity.Comment;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void createPost() {
        //give
        Long userId = 1L;
        AuthUser authUser = new AuthUser(userId, UserRole.USER);

        User user = new User(
                "홍길동",
                "test@test.com",
                "encoded_pw",
                "hkd",
                "01012345678",
                "서울시 여러분 "
        );

        PostCreateRequest request = new PostCreateRequest();
        ReflectionTestUtils.setField(request, "title", "제목");
        ReflectionTestUtils.setField(request, "content", "내용");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        PostCreateResponse response = postService.createPost(authUser, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("제목");
        assertThat(response.getContent()).isEqualTo("내용");
    }

    @Test
    void createPost_NOT_FOUND_USER() {
        //give
        Long userId = 10L;
        AuthUser authUser = new AuthUser(userId, UserRole.USER);

        PostCreateRequest request = new PostCreateRequest();
        ReflectionTestUtils.setField(request, "title", "제목");
        ReflectionTestUtils.setField(request, "content", "내용");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                postService.createPost(authUser, request)
        )
                .isInstanceOf(CustomException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NOT_FOUND_USER);

    }

    @Test
    void readPost() {
        Long postId = 1L;

        User user = new User(
                "홍길동",
                "test@test.com",
                "encoded_pw",
                "hkd",
                "01077777777",
                "서울시 여러분 "
        );

        Post post = new Post(
                "제목",
                "내용",
                user
        );

        ReflectionTestUtils.setField(post, "id", postId);

        Comment comment = new Comment(
                "댓글",
                user,
                post
        );

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId)).thenReturn(List.of(comment));

        PostDetailResponse response = postService.readPost(postId);

        assertThat(response).isNotNull();
        assertThat(response.getPost().getTitle()).isEqualTo("제목");
        assertThat(response.getComments().size()).isEqualTo(1);

    }

    @Test
    void readPost_NOT_FOUND_POST() {
        //give
        Long postId = 10L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                postService.readPost(postId)
        )
                .isInstanceOf(CustomException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NOT_FOUND_POST);
    }

    @Test
    void readPostList() {

        User user = new User(
                "홍길동",
                "test@test.com",
                "encoded_pw",
                "hkd",
                "01077777777",
                "서울시 여러분 "
        );

        Post post = new Post("제목", "내용", user);
        Post post1 = new Post("제목1", "내용1", user);
        Post post2 = new Post("제목2", "내용2", user);
        Post post3 = new Post("제목3", "내용3", user);

        PostDto dto = PostDto.from(post2);
        PostDto dto1 = PostDto.from(post3);

        List<PostDto> posts = List.of(dto1, dto);

        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PostDto> page = new PageImpl<>(posts, pageable, 4);

        when(postRepository.findPostList(any(Pageable.class))).thenReturn(page);

        Page<PostReadResponse> response = postService.readPostList(pageable);

        assertThat(response).isNotNull();
        assertThat(response.getContent().size()).isEqualTo(2);
        assertThat(response.getTotalElements()).isEqualTo(4);
        assertThat(response.getTotalPages()).isEqualTo(2);
        assertThat(response.getNumber()).isEqualTo(0);

        assertThat(response.getContent().get(0).getTitle()).isEqualTo("제목3");

    }

    @Test
    void updatePost() {
        Long userId = 1L;
        Long postId = 1L;
        AuthUser authUser = new AuthUser(userId, UserRole.USER);

        User user = new User(
                "홍길동",
                "test@test.com",
                "encoded_pw",
                "hkd",
                "01012345678",
                "서울시 여러분 "
        );
        ReflectionTestUtils.setField(user, "id", userId);

        Post post = new Post("제목", "내용", user);
        ReflectionTestUtils.setField(post, "id", postId);

        PostUpdateRequest request = new PostUpdateRequest();
        ReflectionTestUtils.setField(request, "title", "제1목");
        ReflectionTestUtils.setField(request, "content", "수1정");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        PostUpdateResponse response = postService.updatePost(postId, authUser, request);
        assertThat(response.getTitle()).isEqualTo("제1목");
        assertThat(response.getContent()).isEqualTo("수1정");

    }

    @Test
    void deletePost() {

        Long userId = 1L;
        Long postId = 1L;
        AuthUser authUser = new AuthUser(userId, UserRole.USER);

        User user = new User(
                "홍길동",
                "test@test.com",
                "encoded_pw",
                "hkd",
                "01012345678",
                "서울시 여러분 "
        );
        ReflectionTestUtils.setField(user, "id", userId);

        Post post = new Post("제목", "내용", user);
        ReflectionTestUtils.setField(post, "id", postId);


        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.deletePost(postId, authUser);
    }

    @Test
    void deletePost_NO_PERMISSION() {
        Long userId = 1L;
        Long postId = 1L;
        AuthUser authUser = new AuthUser(userId, UserRole.USER);

        User user = new User(
                "홍길동",
                "test@test.com",
                "encoded_pw",
                "hkd",
                "01012345678",
                "서울시 여러분 "
        );
        ReflectionTestUtils.setField(user, "id", 2L);

        Post post = new Post("제목", "내용", user);
        ReflectionTestUtils.setField(post, "id", postId);


        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        assertThatThrownBy(() -> postService.deletePost(postId, authUser))
                .isInstanceOf(CustomException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NO_PERMISSION);
    }

    @Test
    void deletePost_NOT_FOUND_POST() {
        //give
        Long postId = 10L;
        Long userId = 1L;
        AuthUser authUser = new AuthUser(userId, UserRole.USER);
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                postService.deletePost(postId,authUser)
        )
                .isInstanceOf(CustomException.class)
                .extracting("exceptionCode")
                .isEqualTo(ExceptionCode.NOT_FOUND_POST);
    }

}