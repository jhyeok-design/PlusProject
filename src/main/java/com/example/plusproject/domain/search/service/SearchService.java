package com.example.plusproject.domain.search.service;

import com.example.plusproject.domain.product.model.response.ProductReadResponse;
import com.example.plusproject.domain.product.repository.ProductCustomRepositoryImpl;
import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.domain.search.entity.Search;
import com.example.plusproject.domain.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    /**
     * 검색어 집계
     */
    @Transactional
    public void recordSearch(String keyword) {

        Search search = searchRepository.findByKeyword(keyword)
                .orElseGet(() -> searchRepository.save(new Search(keyword)));

        search.increaseCount();
    }


    /**
     * 인기 검색어
     */
    @Transactional(readOnly = true)
    public Page<Search> getPopularKeywords(Pageable pageable) {
        return searchRepository.findAllByOrderByCountDesc(pageable);
    }
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
