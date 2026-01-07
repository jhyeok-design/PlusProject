package com.example.plusproject.domain.user.repository;

import com.example.plusproject.domain.user.model.UserDto;
import com.example.plusproject.domain.user.model.response.UserReadResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.plusproject.domain.user.entity.QUser.user;

public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public UserCustomRepositoryImpl(EntityManager em) {
        jpaQueryFactory = new JPAQueryFactory(em);
    }

    //도메인으로 검색
    private BooleanExpression userDomainContains(String domain) {
        return domain != null ? user.email.contains(domain) : null;
    }

    //이름으로 검색
    private BooleanExpression userNameEqual(String name) {
        return name != null ? user.name.eq(name) : null;
    }

    //createdAt 으로 검색
    private BooleanExpression userCreatedAtLoe(LocalDateTime createdAt) {
        return createdAt != null ? user.createdAt.loe(createdAt) : null;
    }

    @Override
    public Page<UserReadResponse> readUserByQuery(Pageable pageable,
                                                  String domain,
                                                  String name,
                                                  LocalDateTime createdAt
    ) {

        List<UserDto> result = jpaQueryFactory
                .select(Projections.constructor(UserDto.class,
                        user.id,
                        user.name,
                        user.email,
                        user.nickname,
                        user.phone,
                        user.address,
                        user.role,
                        user.isDeleted,
                        user.createdAt,
                        user.updatedAt
                ))
                .from(user)
                .where(
                        userDomainContains(domain),
                        userNameEqual(name),
                        userCreatedAtLoe(createdAt)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(user.createdAt.desc())
                .fetch();

        int size = result.size();

        List<UserReadResponse> res = result.stream().map(UserReadResponse::from).toList();

        return new PageImpl<>(res, pageable, size);
    }

}
