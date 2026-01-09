package com.example.plusproject.domain.review.repository;

import com.example.plusproject.domain.review.model.response.ReviewReadResponse;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.plusproject.domain.product.entity.QProduct.product;
import static com.example.plusproject.domain.review.entity.QReview.review;
import static com.example.plusproject.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 해당 상품 id에 따른 리뷰들을 조회 (최신순, 오래된순)
     */
    public Page<ReviewReadResponse> readReviewWithProductSortBy(Long productId, Pageable pageable, String sort) {

        Long total = queryFactory
                .select(review.countDistinct())
                .from(review)
                .join(review.product, product)
                .join(review.user, user)
                .where(review.product.id.eq(productId))
                .fetchOne();

        long totalCount = (total != null) ? total : 0L;

        return new PageImpl<>(
                queryFactory
                        .select(Projections.constructor(ReviewReadResponse.class,
                                review.id,
                                review.user.id,
                                review.user.nickname,
                                review.product.id,
                                review.product.name,
                                review.content,
                                review.score,
                                review.createdAt,
                                review.updatedAt))
                        .from(review)
                        .join(review.product, product)
                        .join(review.user, user)
                        .where(review.product.id.eq(productId))
                        .orderBy(createdOrderSpecifier(sort))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch(),
                pageable,
                totalCount);
    }

    /**
     * 로그인 한 본인의 리뷰 전체 조회 (최신순, 오래된순)
     */
    public Slice<ReviewReadResponse> readReviewWithMeSortBy(Long userId, String keyword, Pageable pageable, String sort) {

        List<ReviewReadResponse> result = queryFactory
                .select(Projections.constructor(ReviewReadResponse.class,
                        review.id,
                        review.user.id,
                        review.user.nickname,
                        review.product.id,
                        review.product.name,
                        review.content,
                        review.score,
                        review.createdAt,
                        review.updatedAt))
                .from(review)
                .join(review.product, product)
                .join(review.user, user)
                .where(
                        review.user.id.eq(userId),
                        containsKeyword(keyword)
                )
                .orderBy(createdOrderSpecifier(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(result, pageable, hasNext(result, pageable.getPageSize()));
    }

    private boolean hasNext(List<ReviewReadResponse> result, int pageSize) {

        if (result.size() > pageSize) {
            result.remove(pageSize);
            return true;
        }

        return false;
    }

    /**
     * 동적 정렬을 위한 OrderSpecifier를 얻는 메소드
     */
    private OrderSpecifier[] createdOrderSpecifier(String sort) {

        List<OrderSpecifier> orderSpecifierList = new ArrayList<>();

        if ("newest".equals(sort)) {
            orderSpecifierList.add(new OrderSpecifier(Order.DESC, review.createdAt));
        } else {
            orderSpecifierList.add(new OrderSpecifier(Order.ASC, review.createdAt));
        }

        return orderSpecifierList.toArray(new OrderSpecifier[0]);
    }

    private BooleanExpression containsKeyword(String keyword) {

        return (keyword != null) ? review.content.contains(keyword) : null;
    }
}
