package com.example.plusproject.domain.search.service;

import com.example.plusproject.domain.product.model.response.ProductReadResponse;
import com.example.plusproject.domain.product.repository.ProductCustomRepositoryImpl;
import com.example.plusproject.domain.search.entity.Search;
import com.example.plusproject.domain.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final ProductCustomRepositoryImpl productCustomRepository;

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

    /**
     * 상품 검색
     */
    @Transactional
    public Page<ProductReadResponse> readProductBySearchQuery(Pageable pageable, String name, Long price) {

        return productCustomRepository.readProductBySearchQuery(pageable, name, price);
    }
}