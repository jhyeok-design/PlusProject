package com.example.plusproject.domain.order.model.response;

import com.example.plusproject.domain.order.model.OrderDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class OrderReadResponse {

    private final Long id;
    private final Long userId;
    private final String productName;
    private final Long productPrice;
    private final Long totalPrice;
    private final LocalDateTime createdAt;

    public static OrderReadResponse from(OrderDto orderDto) {
        return new OrderReadResponse(
                orderDto.getId(),
                orderDto.getUser().getId(),
                orderDto.getProductName(),
                orderDto.getProductPrice(),
                orderDto.getTotalPrice(),
                orderDto.getCreatedAt()
        );
    }
}

