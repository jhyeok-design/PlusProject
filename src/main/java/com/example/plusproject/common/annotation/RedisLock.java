package com.example.plusproject.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

    // Redis 키
    String key();

    // 락의 유효 시간 default: 5s
    long ttl() default 5;

    // 락 획득을 위한 총 대기 시간 default: 3000ms
    long waitTime() default 3000;

    // 재시도 간격 default: 500ms
    long retryInterval() default 500;
}
