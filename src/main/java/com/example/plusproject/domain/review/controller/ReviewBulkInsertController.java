package com.example.plusproject.domain.review.controller;

import com.example.plusproject.domain.review.service.ReviewBulkInsertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review-data")
public class ReviewBulkInsertController {

    private final ReviewBulkInsertService reviewBulkInsertService;

    @PostMapping
    public ResponseEntity<String> bulkInsertReview() {
        return ResponseEntity.ok("500만건 데이터 생성 완료 (소요 시간: " + reviewBulkInsertService.bulkInsert() + ")");
    }
}
