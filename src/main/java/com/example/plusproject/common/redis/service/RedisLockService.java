package com.example.plusproject.common.redis.service;

import com.example.plusproject.common.redis.repository.RedisLockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final RedisLockRepository redisLockRepository;

    /**
     *  락 획득 성공 여부
     */
    public boolean lock(String key, String value, long ttl, long waitTime, long retryInterval) {

        // 락 획득 시도를 시작한 시간
        long startMillis = System.currentTimeMillis();

        // waitTime 동안 반복적으로 락 획득 시도
        while (System.currentTimeMillis() < startMillis + waitTime) {

            // Redis에 락 획득 시도
            if (redisLockRepository.tryLock(key, value, ttl)) {

                // 성공 시 리턴
                return true;
            }

            try {

                // 락을 못 얻었을 경우 대기 후 재시도
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {

                //  현재 스레드의 인터럽트 상태 복구 더 이상 락 획득 시도하지 않음
                Thread.currentThread().interrupt();
                return false;
            }
        }

        // waitTime동안 시도 했지만 실패
        return false;
    }

    /**
     * 락 해제
     */
    public void unlock(String key, String value) {redisLockRepository.unlock(key, value);}
}