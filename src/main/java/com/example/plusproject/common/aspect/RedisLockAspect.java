package com.example.plusproject.common.aspect;

import com.example.plusproject.common.annotation.RedisLock;
import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.redis.service.RedisLockService;
import com.example.plusproject.domain.order.model.request.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisLockAspect {

    private final RedisLockService lockService;

    /**
     * Redis 분산 락을 적용하는 AOP
     */
    @Around("@annotation(redisLock)")
    public Object run(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {

        // 어노테이션에 지정한 값 Redis key ex)PRODUCT_LOCK
        String keyPreFix = redisLock.key();
        // 락 소유자 식별 내가 등록한 락만 해제하기 위함
        String value = UUID.randomUUID().toString();

        // 실행 메서드 파라미터 값
        Object[] args = joinPoint.getArgs();
        String lockId = "default";

        for (Object arg : args) {
            // OrderCreateRequest 상품명
            if (arg instanceof OrderCreateRequest request) {
                lockId = request.getProductName();
                break;
            }
        }

        // 락 키 생성 ex) PRODUCT_LOCK : 파멸의 물의돌_12
        String key = keyPreFix + ":" + lockId;

        // 락 획득 시도
        boolean isLocked = lockService.lock(key, value, redisLock.ttl(), redisLock.waitTime(), redisLock.retryInterval());

        // 락 획득 실패
        if (!isLocked) {
            log.warn("락 획득 실패: {}", key);
            throw new CustomException(ExceptionCode.LOCK_ACQUISITION_FAILED);
        }

        try {
            log.info("락 획득 성공: {}", key);
            // 락 획득 성공 후 메서드 실
            return joinPoint.proceed();
        } finally {
            log.info("락 해제: {}", key);
            // 예외와 상관없이 락 해제
            lockService.unlock(key, value);
        }
    }
}

