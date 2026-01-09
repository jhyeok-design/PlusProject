package com.example.plusproject.domain.product.repository;

import com.example.plusproject.domain.product.model.ProductDto;
import com.example.plusproject.domain.product.model.response.ProductReadResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.plusproject.domain.product.entity.QProduct.product;

@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public ProductCustomRepositoryImpl(EntityManager entityManager) {
        jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<ProductReadResponse> readProductBySearchQuery(Pageable pageable, String name, Long price) {

        BooleanBuilder builder = new BooleanBuilder();

        if (name != null) {
            builder.and(product.name.contains(name));
        }
        if (price != null) {
            builder.and(product.price.loe(price));
        }

        List<ProductDto> lists =
                jpaQueryFactory
                        .select(Projections.constructor(
                                ProductDto.class,
                                product.id,
                                product.name,
                                product.price,
                                product.description,
                                product.quantity,
                                product.createdAt,
                                product.updatedAt
                        ))
                        .from(product)
                        .where(builder)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        long count = lists.size();

        List<ProductReadResponse> response = lists.stream().map(ProductReadResponse::from).toList();

        return new PageImpl<>(response, pageable, count);
    }
}