package com.example.plusproject.domain.review.service;

import com.example.plusproject.common.enums.UserRole;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.domain.product.entity.Product;
import com.example.plusproject.domain.product.repository.ProductRepository;
import com.example.plusproject.domain.review.entity.Review;
import com.example.plusproject.domain.review.model.request.ReviewCreateRequest;
import com.example.plusproject.domain.review.model.response.ReviewCreateResponse;
import com.example.plusproject.domain.review.model.response.ReviewReadResponse;
import com.example.plusproject.domain.review.repository.ReviewQueryRepository;
import com.example.plusproject.domain.review.repository.ReviewRepository;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService; // Test 대상

    @Mock
    private ReviewRepository reviewRepository; // 테스트 대상을 위한 의존성 주입

    @Mock
    private ReviewQueryRepository reviewQueryRepository; // 테스트 대상을 위한 의존성 주입

    @Mock
    private UserRepository userRepository; // 테스트 대상을 위한 의존성 주입

    @Mock
    private ProductRepository productRepository; // 테스트 대상을 위한 의존성 주입

    @Mock
    private ReviewRankingService reviewRankingService; // 테스트 대상을 위한 의존성 주입

    private Long userId = 1L;
    private Long productId = 1L;
    private Long reviewId = 1L;

    private User user;
    private Product product;
    private AuthUser authUser;

    ReviewCreateRequest request;
    private Review review;

    @BeforeEach
    void setUp() {
        user = new User("홍길동", "test@test.com", "1234", "길동이", "010-1234-5678", "경기도");
        product = new Product("화려한 돌맹이", 30000L, "눈부시게 화려한 돌맹이", 5L, "dwwdw");
        authUser = new AuthUser(userId, UserRole.USER);

        request = new ReviewCreateRequest();
        ReflectionTestUtils.setField(request, "productId", productId);
        ReflectionTestUtils.setField(request, "content", "눈부시게 화려한 돌맹이긴 하네요.");
        ReflectionTestUtils.setField(request, "score", 5);

        review = new Review(user, product, request.getContent(), request.getScore());
    }

    @Test
    @DisplayName("리뷰 생성에 성공한다.")
    void 리뷰_생성_성공_테스트() {

        // 가짜 객체가 어떻게 행동할지 정함
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(reviewRepository.save(any(Review.class))).willReturn(review);

        // When
        ReviewCreateResponse response = reviewService.createReview(authUser, request);

        // Then
        assertThat(response.getContent()).isEqualTo(request.getContent());
        assertThat(response.getScore()).isEqualTo(request.getScore());

        verify(userRepository, times(1)).findById(userId);
        verify(productRepository, times(1)).findById(productId);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    @DisplayName("존재하지 않는 상품인 경우 리뷰 생성에 실패한다.")
    void 존재하지_않는_상품_리뷰_생성_실패_테스트() {

        // Given
        ReviewCreateRequest request = new ReviewCreateRequest();
        ReflectionTestUtils.setField(request, "productId", productId);
        ReflectionTestUtils.setField(request, "content", "눈부시게 화려한 돌맹이긴 하네요.");
        ReflectionTestUtils.setField(request, "score", 5);

        // 가짜 객체가 어떻게 행동할지 정함
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // Then
        assertThrows(CustomException.class, () -> {
            reviewService.createReview(authUser, request);
        });

    }

    @Test
    @DisplayName("리뷰 단 건 조회에 성공한다.")
    void 리뷰_단건_조회_성공_테스트() {

        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        ReviewReadResponse response = reviewService.readReview(reviewId);

        assertThat(response.getContent()).isEqualTo(review.getContent());

        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewRankingService, times(1)).increaseViewCount(review.getProduct().getId(), reviewId);
    }

}