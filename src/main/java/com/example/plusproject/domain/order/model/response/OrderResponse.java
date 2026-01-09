package com.example.plusproject.domain.order.model.response;

import com.example.plusproject.domain.order.entity.Order;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderResponse {

    private final Long orderId;
    private final String productName;
    private final Long price;
    private final String username;

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProduct().getName(),
                order.getProductPrice(),
                order.getUser().getName()
        );
    }
}