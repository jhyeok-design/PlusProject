package com.example.plusproject.domain.order.service;

import com.example.plusproject.domain.order.model.response.OrderPageResponse;
import com.example.plusproject.domain.order.model.response.OrderResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String CACHE_ORDER_PREFIX = "keyword:";

    // 캐시를 조회하는 것
    public OrderPageResponse<OrderResponse> getOrderCache(String keyword, int page, int size) {
        // 이 key를 기준으로 redis에 있는 데이터를 불러올 것이다.
        String key = CACHE_ORDER_PREFIX + keyword + ":" + page + ":" + size;
        // keyword:화강암 keyword:보석암

        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return null;

        return objectMapper.convertValue(
                value,
                new TypeReference<OrderPageResponse<OrderResponse>>() {}
        );
    }

    // 캐시를 저장하는 것
    public void saveOrderCache(String keyword, int page, int size, OrderPageResponse<OrderResponse> orderResponse) {
        String key = CACHE_ORDER_PREFIX + keyword + ":" + page + ":" + size;
        redisTemplate.opsForValue().set(key, orderResponse, 10, TimeUnit.MINUTES);
    }
}
