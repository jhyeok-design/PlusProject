package com.example.plusproject.domain.order.repository;

import com.example.plusproject.domain.order.entity.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.plusproject.domain.order.entity.QOrder.order;
import static com.example.plusproject.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> findAllWithUser(Pageable pageable) {
        List<Order> content = queryFactory
                .selectFrom(order)
                .join(order.user, user).fetchJoin()
                .where(order.isDeleted.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(order.count())
                .from(order)
                .where(order.isDeleted.eq(false))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}