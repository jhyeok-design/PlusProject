package com.example.plusproject.domain.order.service;

import com.example.plusproject.common.annotation.RedisLock;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.domain.order.model.request.OrderCreateRequest;
import com.example.plusproject.domain.order.model.response.OrderCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderRedisService {

    private final OrderService orderService;

    /**
     * 락을 먼저 획득하고 내부의 트랜잭션 서비스를 호출
     */
    @RedisLock(key = "PRODUCT_LOCK", ttl = 10, waitTime = 5000, retryInterval = 100)
    public OrderCreateResponse createOrderWithLock(AuthUser authUser, OrderCreateRequest request) {

        return orderService.createOrder(authUser, request);
    }
}
