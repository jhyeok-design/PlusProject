package com.example.plusproject.domain.comment.repository;

import com.example.plusproject.domain.comment.model.CommentDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.plusproject.domain.comment.entity.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommentDto> findCommentList(Long postId, Long userId, Pageable pageable) {

        List<CommentDto> commentList = queryFactory
                .select(Projections.constructor(
                        CommentDto.class,
                        comment.id,
                        comment.user.id,
                        comment.post.id,
                        comment.content,
                        comment.createdAt,
                        comment.updatedAt
                ))
                .from(comment)
                .where(comment.post.id.eq(postId),
                        comment.user.id.eq(userId))
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.post.id.eq(postId),
                        comment.user.id.eq(userId))
                .fetchOne();

        long safeTotal = total != null ? total : 0L;

        return new PageImpl<>(commentList, pageable, safeTotal);
    }
}
