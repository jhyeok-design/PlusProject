package com.example.plusproject.domain.review.service;

import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.SliceResponse;
import com.example.plusproject.domain.review.model.response.ReviewReadResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewCacheService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Redis에서 유저 별 리뷰 목록을 읽어오는 메소드
     */
    public SliceResponse<ReviewReadResponse> readReviewWithMeCache(
            AuthUser authUser,
            String keyword,
            Integer page, Integer size,
            String sort) {

        String key = "user:" + authUser.getUserId() + ":keyword:" + keyword + ":page:" + page + ":size:" + size + ":sort:" + sort;

        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return null;
        }

        try {
            return objectMapper.readValue(value, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.warn("[캐시 역직렬화 실패] DB에서 데이터 조회", e);
            return null;
        }
    }

    /**
     * Redis에 유저 별 리뷰 목록을 저장하는 메소드
     */
    public void saveReviewWithMeCache(
            AuthUser authUser,
            String keyword,
            Integer page, Integer size,
            String sort,
            SliceResponse<ReviewReadResponse> response) {

        String key = "user:" + authUser.getUserId() + ":keyword:" + keyword + ":page:" + page + ":size:" + size + ":sort:" + sort;

        try {
            String value = objectMapper.writeValueAsString(response);

            redisTemplate.opsForValue().set(key, value, 10, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            log.warn("[직렬화 실패] 캐시 저장 건너뛰기", e);
        }
    }
}