package com.example.plusproject.domain.review.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.SliceResponse;
import com.example.plusproject.domain.product.entity.Product;
import com.example.plusproject.domain.product.repository.ProductRepository;
import com.example.plusproject.domain.review.entity.Review;
import com.example.plusproject.domain.review.model.ReviewDto;
import com.example.plusproject.domain.review.model.request.ReviewCreateRequest;
import com.example.plusproject.domain.review.model.request.ReviewUpdateRequest;
import com.example.plusproject.domain.review.model.response.ReviewCreateResponse;
import com.example.plusproject.domain.review.model.response.ReviewReadResponse;
import com.example.plusproject.domain.review.model.response.ReviewUpdateResponse;
import com.example.plusproject.domain.review.repository.ReviewQueryRepository;
import com.example.plusproject.domain.review.repository.ReviewRepository;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ReviewQueryRepository reviewQueryRepository;
    private final ReviewCacheService reviewCacheService;
    private final ReviewRankingService reviewRankingService;

    /**
     * 리뷰 생성 비즈니스 로직
     */
    @Transactional
    public ReviewCreateResponse createReview(AuthUser authUser, ReviewCreateRequest request) {

        // 유저 존재 여부 검증 및 유저 가져오기
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        // 상품 존재 여부 검증 및 상품 가져오기
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_PRODUCT));

        Review review = new Review(
                user,
                product,
                request.getContent(),
                request.getScore());

        ReviewDto savedReview = ReviewDto.from(reviewRepository.save(review));

        return ReviewCreateResponse.from(savedReview);
    }

    /**
     * 리뷰 단 건 조회 비즈니스 로직
     */
    @Transactional(readOnly = true)
    public ReviewReadResponse readReview(Long reviewId) {
        
        Review review = getReview(reviewId);

        ReviewDto foundReview = ReviewDto.from(review);

        reviewRankingService.increaseViewCount(foundReview.getProduct().getId(), reviewId);

        return ReviewReadResponse.from(foundReview);
    }

    /**
     * 상품 별 리뷰 전체 조회 v2
     * 로컬 캐시로 성능 개선
     */
    @Cacheable(
            value = "productReviewCache",
            key = "'product:' + #productId + ':page:' + #page + ':size:' + #size + ':sort:' + #sort")
    @Transactional(readOnly = true)
    public Page<ReviewReadResponse> readReviewWithProduct(Long productId, Integer page, Integer size, String sort) {

        Sort.Direction direction = "newest".equals(sort) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));

        return reviewQueryRepository.readReviewWithProductSortBy(productId, pageable, sort);
    }

//    /**
//     * 유저 별 리뷰 전체 조회 (내 리뷰 전체 조회) v1
//     */
//    @Transactional(readOnly = true)
//    public SliceResponse<ReviewReadResponse> readReviewWithMe(AuthUser authUser, String keyword, Integer page, Integer size, String sort) {
//
//        Sort.Direction direction = "newest".equals(sort) ? Sort.Direction.DESC : Sort.Direction.ASC;
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
//
//        Slice<ReviewReadResponse> response = reviewQueryRepository.readReviewWithMeSortBy(authUser.getUserId(), keyword, pageable, sort);
//
//        return SliceResponse.from(response);
//
//    }

//    /**
//     * 유저 별 리뷰 전체 조회 (내 리뷰 전체 조회) v2
//     * Redis 캐시로 성능 개선
//     */
//    @Cacheable(
//            value = "myReviewCache",
//            key = "'user:' + #authUser.userId + ':keyword:' + #keyword + ':page:' + #page + ':size:' + #size + ':sort:' + #sort")
//    @Transactional(readOnly = true)
//    public SliceResponse<ReviewReadResponse> readReviewWithMe(AuthUser authUser, String keyword, Integer page, Integer size, String sort) {
//
//        Sort.Direction direction = "newest".equals(sort) ? Sort.Direction.DESC : Sort.Direction.ASC;
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
//
//        Slice<ReviewReadResponse> response = reviewQueryRepository.readReviewWithMeSortBy(authUser.getUserId(), keyword, pageable, sort);
//
//        return SliceResponse.from(response);
//    }

    /**
     * 유저 별 리뷰 전체 조회 (내 리뷰 전체 조회) v3
     * Redis 캐시로 성능 개선
     */
    @Transactional(readOnly = true)
    public SliceResponse<ReviewReadResponse> readReviewWithMe(AuthUser authUser, String keyword, Integer page, Integer size, String sort) {

        // Redis에서 읽기
        SliceResponse<ReviewReadResponse> cached = reviewCacheService.readReviewWithMeCache(authUser, keyword, page, size, sort);

        // Redis에 있으면 바로 리턴
        if (cached != null) {
            return cached;
        }

        Sort.Direction direction = "newest".equals(sort) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));

        Slice<ReviewReadResponse> response = reviewQueryRepository.readReviewWithMeSortBy(authUser.getUserId(), keyword, pageable, sort);

        SliceResponse<ReviewReadResponse> sliceResponse = SliceResponse.from(response);

        // Redis에 저장
        reviewCacheService.saveReviewWithMeCache(authUser, keyword, page, size, sort, sliceResponse);

        return sliceResponse;
    }

    /**
     * 리뷰 수정 비즈니스 로직
     */
    @Caching(evict = {
            @CacheEvict(value = "productReviewCache", allEntries = true),
            @CacheEvict(value = "myReviewCache", allEntries = true)
    })
    @Transactional
    public ReviewUpdateResponse updateReview(AuthUser authUser, Long reviewId, ReviewUpdateRequest request) {

        Review review = getReview(reviewId);

        // 본인이 작성한 리뷰인지 권한 검증
        if (!authUser.getUserId().equals(review.getUser().getId())) {
            throw new CustomException(ExceptionCode.NO_PERMISSION);
        }

        review.update(request);

        ReviewDto updatedReview = ReviewDto.from(review);

        return ReviewUpdateResponse.from(updatedReview);
    }

    /**
     * 리뷰 삭제 비즈니스 로직
     */
    @Caching(evict = {
            @CacheEvict(value = "productReviewCache", allEntries = true),
            @CacheEvict(value = "myReviewCache", allEntries = true)
    })
    @Transactional
    public void deleteReview(AuthUser authUser, Long reviewId) {

        Review review = getReview(reviewId);

        // 본인이 작성한 리뷰인지 권한 검증
        if (!authUser.getUserId().equals(review.getUser().getId())) {
            throw new CustomException(ExceptionCode.NO_PERMISSION);
        }

        reviewRepository.delete(review);
    }

    /**
     * 리뷰 존재 여부 검증 및 가져오기
     */
    private Review getReview(Long reviewId) {

        // 리뷰 존재 여부 검증 및 수정할 리뷰 가져오기
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_REVIEW));
    }
}
