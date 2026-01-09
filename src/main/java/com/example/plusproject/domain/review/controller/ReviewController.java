package com.example.plusproject.domain.review.controller;

import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.common.model.SliceResponse;
import com.example.plusproject.domain.review.model.request.ReviewCreateRequest;
import com.example.plusproject.domain.review.model.request.ReviewUpdateRequest;
import com.example.plusproject.domain.review.model.response.ReviewCreateResponse;
import com.example.plusproject.domain.review.model.response.ReviewReadResponse;
import com.example.plusproject.domain.review.model.response.ReviewUpdateResponse;
import com.example.plusproject.domain.review.service.ReviewRankingService;
import com.example.plusproject.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewRankingService reviewRankingService;

    /**
     * 리뷰 생성
     */
    @PostMapping
    public ResponseEntity<CommonResponse<ReviewCreateResponse>> createReview(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody ReviewCreateRequest request) {

        ReviewCreateResponse response = reviewService.createReview(authUser, request);

        return ResponseEntity.ok(CommonResponse.success("리뷰 생성 완료", response));
    }

    /**
     * 리뷰 단건 조회
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<ReviewReadResponse>> readReview(@PathVariable Long reviewId) {

        ReviewReadResponse response = reviewService.readReview(reviewId);

        return ResponseEntity.ok(CommonResponse.success("리뷰 단 건 조회 완료", response));
    }

    /**
     * 상품별 리뷰 전체 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse<Page<ReviewReadResponse>>> readReviewWithProduct(@RequestParam Long productId, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "newest") String sort) {

        Page<ReviewReadResponse> response = reviewService.readReviewWithProduct(productId, page, size, sort);

        return ResponseEntity.ok(CommonResponse.success("상품 별 리뷰 전체 조회 완료", response));
    }

//    /**
//     * 유저 별 리뷰 전체 조회 (내 리뷰 전체 조회) v1
//     */
//    @GetMapping("/my-review")
//    public ResponseEntity<CommonResponse<SliceResponse<ReviewReadResponse>>> readReviewWithMeV1(@AuthenticationPrincipal AuthUser authUser, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "newest") String sort) {
//
//        SliceResponse<ReviewReadResponse> response = reviewService.readReviewWithMeV1(authUser, keyword, page, size, sort);
//
//        return ResponseEntity.ok(CommonResponse.success("내 리뷰 전체 조회 완료", response));
//    }

    /**
     * 유저 별 리뷰 전체 조회 (내 리뷰 전체 조회) - v2 (In-memory Cache)
     */
    @GetMapping("/my-review")
    public ResponseEntity<CommonResponse<SliceResponse<ReviewReadResponse>>> readReviewWithMeV2(@AuthenticationPrincipal AuthUser authUser, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "newest") String sort) {

        SliceResponse<ReviewReadResponse> response = reviewService.readReviewWithMeV2(authUser, keyword, page, size, sort);

        return ResponseEntity.ok(CommonResponse.success("내 리뷰 전체 조회 완료", response));
    }

//    /**
//     * 유저 별 리뷰 전체 조회 (내 리뷰 전체 조회) - v3 (Redis 를 이용한 Remote Cache)
//     */
//    @GetMapping("/my-review")
//    public ResponseEntity<CommonResponse<SliceResponse<ReviewReadResponse>>> readReviewWithMeV3(@AuthenticationPrincipal AuthUser authUser, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "newest") String sort) {
//
//        SliceResponse<ReviewReadResponse> response = reviewService.readReviewWithMeV3(authUser, keyword, page, size, sort);
//
//        return ResponseEntity.ok(CommonResponse.success("내 리뷰 전체 조회 완료", response));
//    }


    /**
     * 리뷰 조회 수 순위 조회
     */
    @GetMapping("/popular")
    public ResponseEntity<CommonResponse<List<ReviewReadResponse>>> readPopularReviewTop10(@RequestParam Long productId) {

        List<ReviewReadResponse> responses = reviewRankingService.readPopularReviewTop10(productId);
        return ResponseEntity.ok(CommonResponse.success("조회수 TOP 10 리뷰 조회 완료", responses));
    }

    /**
     * 리뷰 수정
     */
    @PatchMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<ReviewUpdateResponse>> updateReview(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long reviewId, @RequestBody ReviewUpdateRequest request) {
        ReviewUpdateResponse response = reviewService.updateReview(authUser, reviewId, request);
        return ResponseEntity.ok(CommonResponse.success("리뷰 수정 완료", response));
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<CommonResponse<Void>> deleteReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long reviewId) {

        reviewService.deleteReview(authUser, reviewId);

        return ResponseEntity.ok(CommonResponse.success("리뷰 삭제 완료", null));
    }
}
