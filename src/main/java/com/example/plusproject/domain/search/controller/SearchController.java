package com.example.plusproject.domain.search.controller;

import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.product.model.response.ProductReadResponse;
import com.example.plusproject.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * 상품 검색
     */
    @GetMapping("/api/search")
    public ResponseEntity<?> readProductBySearchQuery(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String name,
            @RequestParam Long price,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
            ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductReadResponse> response = searchService.readProductBySearchQuery(
                pageable,
                name,
                price,
                startDate,
                endDate
        );
        return ResponseEntity.ok(CommonResponse.success("검색 완료", response));
    }
}
