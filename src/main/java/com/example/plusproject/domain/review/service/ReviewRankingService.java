package com.example.plusproject.domain.review.service;

import com.example.plusproject.domain.review.entity.Review;
import com.example.plusproject.domain.review.model.ReviewDto;
import com.example.plusproject.domain.review.model.ReviewRankingDto;
import com.example.plusproject.domain.review.model.response.ReviewReadResponse;
import com.example.plusproject.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReviewRankingService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ReviewRepository reviewRepository;

    public void increaseViewCount(Long productId, Long reviewId) {

        redisTemplate.opsForZSet().incrementScore("productId:" + productId + ":review:view:ranking", reviewId.toString(), 1);
    }

    @Transactional(readOnly = true)
    public List<ReviewReadResponse> readPopularReviewTop10(Long productId) {

        Set<ZSetOperations.TypedTuple<String>> result = redisTemplate.opsForZSet().reverseRangeWithScores("productId:" + productId + ":review:view:ranking", 0, 9);
        System.out.println(result);

        if (result == null || result.isEmpty()) {
            return Collections.emptyList();
        }

        List<ReviewRankingDto> rankingDtos = result.stream()
                .map(tuple -> new ReviewRankingDto(Long.parseLong(tuple.getValue()), tuple.getScore().longValue()))
                .toList();

        List<Long> reviewIds = rankingDtos.stream()
                .map(ReviewRankingDto::getReviewId)
                .toList();

        List<Review> reviews = reviewRepository.findByIdIn(reviewIds);

        List<ReviewDto> list = reviews.stream()
                .map(ReviewDto::from)
                .toList();

        return list.stream()
                .map(ReviewReadResponse::from)
                .toList();
    }
}
