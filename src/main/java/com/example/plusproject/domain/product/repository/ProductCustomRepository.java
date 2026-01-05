package com.example.plusproject.domain.product.repository;
import com.example.plusproject.domain.product.model.response.ProductReadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface ProductCustomRepository {
    Page<ProductReadResponse> readProductBySearchQuery(Pageable pageable,
                                                       String name,
                                                       Long price
    );
}
