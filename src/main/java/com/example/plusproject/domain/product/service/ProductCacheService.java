package com.example.plusproject.domain.product.service;

import com.example.plusproject.domain.product.model.response.ProductReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProductCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_PRODUCT_PREFIX = "product:";

    @SuppressWarnings("unchecked")
    public List<ProductReadResponse> readProductCache(String name) {
        String key = CACHE_PRODUCT_PREFIX + name.trim().replaceAll("\\s+", "");
        return (List<ProductReadResponse>) redisTemplate.opsForValue().get(key);
    }

    public void saveProductCache(String name, List<ProductReadResponse> responses) {
        String key = CACHE_PRODUCT_PREFIX + name.trim().replaceAll("\\s+", "");
        redisTemplate.opsForValue().set(key, responses, 10, TimeUnit.MINUTES);
    }
}
