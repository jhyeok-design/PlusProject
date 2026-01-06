package com.example.plusproject.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 로컬 캐시 TTL 설정
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES) // 쓰여지고 10분 후 만료
                .maximumSize(10000)); // 캐시 최대 항목 수 10000개

        cacheManager.setCacheNames(List.of("productReviewCache", "myReviewCache")); // 특정 캐시만 적용

        return cacheManager;
    }
}
