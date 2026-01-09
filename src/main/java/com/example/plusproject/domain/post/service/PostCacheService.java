package com.example.plusproject.domain.post.service;

import com.example.plusproject.domain.post.model.response.PostReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PostCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_PRODUCT_PREFIX = "post:";

    private String key(String keyword, String nickname) {
        return CACHE_PRODUCT_PREFIX
                + (keyword != null ? keyword : "ALL")
                + ", "
                + (nickname != null ? nickname : "ALL");
    }

    @SuppressWarnings("unchecked")
    public List<PostReadResponse> readPostCache(String keyword, String nickname) {

        return (List<PostReadResponse>) redisTemplate.opsForValue().get(key(keyword, nickname));
    }

    public void savePostCache(String keyword, String nickname, List<PostReadResponse> responses) {

        redisTemplate.opsForValue().set(key(keyword, nickname), responses, 10, TimeUnit.MINUTES);
    }

    public void evictPost() {

        Set<String> keys = redisTemplate.keys("post:*");

        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
