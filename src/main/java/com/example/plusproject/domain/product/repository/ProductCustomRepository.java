package com.example.plusproject.domain.product.repository;

import com.example.plusproject.domain.product.entity.Product;
import com.example.plusproject.domain.product.model.response.ProductReadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ProductCustomRepository {
    Page<ProductReadResponse> readProductBySearchQuery(Pageable pageable,
                                                       String name,
                                                       Long price,
                                                       LocalDateTime startDate,
                                                       LocalDateTime endDate
    );
}
