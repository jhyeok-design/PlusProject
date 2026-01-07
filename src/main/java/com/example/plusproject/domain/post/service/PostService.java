package com.example.plusproject.domain.post.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.domain.comment.model.CommentDto;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final PostCacheService postCacheService;

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
                .map(CommentDto::from)
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
        postCacheService.evictPost();
        return PostUpdateResponse.from(dto);
    }

    @Transactional
    public void deletePost(Long postId, AuthUser authUser) {

        Post post = getPostWithPermission(postId, authUser.getUserId());

        commentRepository.deleteByPostId(postId);
        postRepository.delete(post);
        postCacheService.evictPost();
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

    @Transactional(readOnly = true)
    public Page<PostReadResponse> searchPostPage(String keyword, String nickname, Pageable pageable) {
        Page<PostDto> page = postRepository.searchByConditions(keyword, nickname, pageable);

        return page.map(PostReadResponse::from);
    }

//    @Cacheable(value = "postCache",
//            key = "'keyword=' + (#keyword != null ? #keyword : 'ALL') + " +
//                    "', nickname=' + (#nickname != null ? #nickname : 'ALL')",
//            condition = "#pageable.pageNumber == 0"
//    )
//    @Transactional(readOnly = true)
//    public Page<PostReadResponse> searchPostPageV2(String keyword, String nickname, Pageable pageable) {
//
//        Page<PostDto> page = postRepository.searchByConditions(keyword, nickname, pageable);
//
//        return page.map(PostReadResponse::from);
//    }

    @Transactional(readOnly = true)
    public Page<PostReadResponse> searchPostPageV3(String keyword, String nickname, Pageable pageable) {

        if (pageable.getPageNumber()==0){
            List<PostReadResponse> cached = postCacheService.readPostCache(keyword,nickname);
            if (cached!=null){
                return new PageImpl<>(cached,pageable,cached.size());
            }
        }

        Page<PostDto> page = postRepository.searchByConditions(keyword, nickname, pageable);

        Page<PostReadResponse> result = page.map(PostReadResponse::from);

        if(pageable.getPageNumber()==0){
            postCacheService.savePostCache(keyword, nickname,result.getContent());
        }
        return result;
    }
}
