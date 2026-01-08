package com.example.plusproject.domain.user.service;

import com.example.plusproject.domain.user.model.response.UserReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String USERSEARCH_PREFIX = "userSearch:domain=%s:name=%s:page=%d:size=%d:createdAt=%s";

    private String key(String domain, String name, int page, int size, LocalDateTime createdAt) {

        return String.format(USERSEARCH_PREFIX, domain, name, page, size, createdAt != null ? createdAt.toString() : "null");
    }

    @SuppressWarnings("unchecked")
    public List<UserReadResponse> readUserCache(String domain, String name, int page, int size, LocalDateTime createdAt) {

        return (List<UserReadResponse>) redisTemplate.opsForValue().get(key(domain, name, page, size, createdAt));
    }

    public void saveUserCache(String domain, String name, int page, int size, LocalDateTime createdAt, List<UserReadResponse> value) {

        redisTemplate.opsForValue().set(key(domain, name, page, size, createdAt), value, Duration.ofMinutes(3));
    }
}