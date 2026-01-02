package com.example.plusproject.domain.post.repository;

import com.example.plusproject.domain.post.model.PostDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.plusproject.domain.post.entity.QPost.post;
import static com.example.plusproject.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class PostRepositoryImpl implements CustomPostRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostDto> findPostList(Pageable pageable) {
        List<PostDto> postList = queryFactory
                .select(Projections.constructor(
                        PostDto.class,
                        post.id,
                        post.user.id,
                        post.title,
                        post.content,
                        post.createdAt,
                        post.updatedAt
                ))
                .from(post)
                .join(post.user, user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdAt.desc())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .fetchOne();

        long safeTotal = total != null ? total : 0L;

        return new PageImpl<>(postList, pageable, safeTotal);
    }
}
