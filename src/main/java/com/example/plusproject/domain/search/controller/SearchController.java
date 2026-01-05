package com.example.plusproject.domain.search.controller;

import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.search.entity.Search;
import com.example.plusproject.domain.search.service.SearchService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    /**
     * 인기 검색어
     */
    @GetMapping("/popular")
    public ResponseEntity<CommonResponse<Page<Search>>> search(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<Search> response = searchService.getPopularKeywords(pageable);

        return ResponseEntity.ok(CommonResponse.success("인기 검색어 조회 성공", response));
    }
}
