package com.example.plusproject.domain.order.repository;

import com.example.plusproject.domain.order.entity.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.plusproject.domain.order.entity.QOrder.order;
import static com.example.plusproject.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Order> findAllWithUser() {
        return queryFactory
                .selectFrom(order)
                .join(order.user, user).fetchJoin()
                .where(order.isDeleted.eq(false))
                .fetch();
    }
}
