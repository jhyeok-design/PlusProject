package com.example.plusproject.domain.search.service;

import com.example.plusproject.domain.product.model.response.ProductReadResponse;
import com.example.plusproject.domain.product.repository.ProductCustomRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ProductCustomRepositoryImpl productCustomRepository;
    /**
     * 상품 검색
     */
    @Transactional
    public Page<ProductReadResponse> readProductBySearchQuery(Pageable pageable,
                                         String name,
                                         Long price,
                                         LocalDateTime startDate,
                                         LocalDateTime endDate
    ) {
        // 1. 서치쿼리에 보낸 후 리턴
        return productCustomRepository.readProductBySearchQuery(pageable,
                name,
                price,
                startDate,
                endDate);

    }
}
