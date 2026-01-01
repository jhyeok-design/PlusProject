package com.example.plusproject.domain.post.repository;

import com.example.plusproject.domain.post.model.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
    Page<PostDto> findPostList(Pageable pageable);
}
