package com.example.plusproject.domain.order.model;

import com.example.plusproject.domain.order.entity.Order;
import com.example.plusproject.domain.product.entity.Product;
import com.example.plusproject.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private String productName;
    private Long productPrice;
    private Long totalPrice;
    private User user;
    private Product product;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
