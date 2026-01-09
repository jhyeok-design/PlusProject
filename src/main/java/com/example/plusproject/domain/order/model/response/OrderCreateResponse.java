package com.example.plusproject.domain.order.model.response;

import com.example.plusproject.domain.order.model.OrderDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class OrderCreateResponse {

    private final Long id;
    private final Long userId;
    private final String productName;
    private final Long productPrice;
    private final Long totalPrice;
    private final LocalDateTime createdAt;

    public static OrderCreateResponse from(OrderDto orderDto) {
        return new OrderCreateResponse(
                orderDto.getId(),
                orderDto.getUser().getId(),
                orderDto.getProductName(),
                orderDto.getProductPrice(),
                orderDto.getTotalPrice(),
                orderDto.getCreatedAt()
        );
    }
}
