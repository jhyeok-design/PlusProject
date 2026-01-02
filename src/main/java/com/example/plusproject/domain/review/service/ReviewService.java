package com.example.plusproject.domain.review.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.domain.product.entity.Product;
import com.example.plusproject.domain.product.repository.ProductRepository;
import com.example.plusproject.domain.review.entity.Review;
import com.example.plusproject.domain.review.model.ReviewDto;
import com.example.plusproject.domain.review.model.request.ReviewCreateRequest;
import com.example.plusproject.domain.review.model.response.ReviewCreateResponse;
import com.example.plusproject.domain.review.model.response.ReviewReadResponse;
import com.example.plusproject.domain.review.repository.ReviewRepository;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

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

        Review savedReview = new Review(
                user,
                product,
                request.getContent(),
                request.getScore());

        ReviewDto savedReviewDto = ReviewDto.from(reviewRepository.save(savedReview));

        return ReviewCreateResponse.from(savedReviewDto);
    }

    /**
     * 리뷰 단 건 조회 비즈니스 로직
     */
    @Transactional(readOnly = true)
    public ReviewReadResponse readReview(Long reviewId) {

        // 리뷰 존재 여부 검증
        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_REVIEW));

        ReviewDto foundReviewDto = ReviewDto.from(foundReview);

        return ReviewReadResponse.from(foundReviewDto);
    }
}
