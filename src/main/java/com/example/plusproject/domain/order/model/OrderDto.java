package com.example.plusproject.domain.order.model;

import com.example.plusproject.domain.order.entity.Order;
import com.example.plusproject.domain.product.entity.Product;
import com.example.plusproject.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class OrderDto {

    private final Long id;
    private final  String productName;
    private final Long productPrice;
    private final Long totalPrice;
    private final User user;
    private final Product product;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static OrderDto from(Order order) {
        return new OrderDto(
                order.getId(),
                order.getProductName(),
                order.getProductPrice(),
                order.getTotalPrice(),
                order.getUser(),
                order.getProduct(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
