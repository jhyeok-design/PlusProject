package com.example.plusproject.common.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Collections;

@Repository
@RequiredArgsConstructor
public class RedisLockRepository {

    private final StringRedisTemplate redisTemplate;


    /**
     * 락 획득 시도
     */
    public boolean tryLock(String key, String value, long timeoutSeconds) {

        // key가 존재하지 않을 때만 set (락 선점)
        // timeoutSeconds 후 자동 만료 (데드락 방지)
        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(key, value, Duration.ofSeconds(timeoutSeconds));

        // Boolean 비교 시 NPE 방지
        return Boolean.TRUE.equals(result);
    }

    /**
     * 내가 획득한 락만 해제
     */
    public void unlock(String key, String value) {

        String script =
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                        "   return redis.call('del', KEYS[1]) " +
                        "else " +
                        "   return 0 " +
                        "end";

        // value가 같을 때만 실행 아니면 작동 안함
        redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(key),
                value
        );
    }
}
