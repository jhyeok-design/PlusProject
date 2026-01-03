package com.example.plusproject.domain.review.repository;

import com.example.plusproject.domain.review.model.response.ReviewReadResponse;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public Page<ReviewReadResponse> readReviewWithMeSortBy(Long userId, Pageable pageable, String sort) {

        Long total = queryFactory
                .select(review.countDistinct())
                .from(review)
                .join(review.product, product)
                .join(review.user, user)
                .where(review.user.id.eq(userId))
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
                        .where(review.user.id.eq(userId))
                        .orderBy(createdOrderSpecifier(sort))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch(),
                pageable,
                totalCount);
    }

    
    /**
     * 동적 정렬을 위한 OrderSpecifier를 얻는 메소드
     */
    private OrderSpecifier[] createdOrderSpecifier(String sort) {
        List<OrderSpecifier> orderSpecifierList = new ArrayList<>();

        // sort가 null이면 NPE 발생하므로 리터럴을 앞에 두는 습관 들이기
        if ("newest".equals(sort)) {
            orderSpecifierList.add(new OrderSpecifier(Order.DESC, review.createdAt));
        } else {
            orderSpecifierList.add(new OrderSpecifier(Order.ASC, review.createdAt));
        }

        return orderSpecifierList.toArray(new OrderSpecifier[0]);
    }
}
