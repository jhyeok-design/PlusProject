package com.example.plusproject.domain.review.controller;

import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.review.model.request.ReviewCreateRequest;
import com.example.plusproject.domain.review.model.request.ReviewUpdateRequest;
import com.example.plusproject.domain.review.model.response.ReviewCreateResponse;
import com.example.plusproject.domain.review.model.response.ReviewReadResponse;
import com.example.plusproject.domain.review.model.response.ReviewUpdateResponse;
import com.example.plusproject.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 생성 API
     */
    @PostMapping
    public ResponseEntity<CommonResponse<ReviewCreateResponse>> createReview(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody ReviewCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success("리뷰 생성 완료", reviewService.createReview(authUser, request)));
    }

    /**
     * 리뷰 단 건 조회 API
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<ReviewReadResponse>> readReview(@PathVariable Long reviewId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success("리뷰 단 건 조회 완료", reviewService.readReview(reviewId)));
    }

    /**
     * 리뷰 수정 API
     */
    @PatchMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<ReviewUpdateResponse>> updateReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequest request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success("리뷰 수정 완료", reviewService.updateReview(authUser, reviewId, request)));
    }

    /**
     * 리뷰 삭제 API
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<Void>> deleteReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long reviewId) {

        reviewService.deleteReview(authUser, reviewId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success("리뷰 삭제 완료", null));
    }
}
