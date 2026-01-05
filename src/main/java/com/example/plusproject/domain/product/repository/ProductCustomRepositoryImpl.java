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

    // postCustomRepository 의 메소드를 override 로 구현

    @Override
    public Page<ProductReadResponse> readProductBySearchQuery(Pageable pageable,
                                                              String name,
                                                              Long price
    ) {

        // 1. booleanBuilder 생성
        BooleanBuilder builder = new BooleanBuilder();

        // 2. 변수 null 검증 후 build
        if (name != null) { //이름 포함하는 쿼리
            builder.and(product.name.contains(name));   //contain 많이 사용/ like 는 prefix, 접두사, 접미사
        }
        if (price != null) {    //조회한 가격보다 같거나 낮은 쿼리
            builder.and(product.price.loe(price));
        }

        //3. Product list 화
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
        //page 로 리턴
        return new PageImpl<>(response,pageable,count);
    }

}
