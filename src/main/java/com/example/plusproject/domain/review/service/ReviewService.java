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

        // 유저 존재 여부 검증
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        // 상품 존재 여부 검증
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
}
