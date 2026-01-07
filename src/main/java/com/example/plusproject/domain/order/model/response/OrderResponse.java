package com.example.plusproject.domain.order.model.response;

import com.example.plusproject.domain.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor   // 기본 생성자 (Jackson용)
@AllArgsConstructor  // 전체 필드 생성자
public class OrderResponse {

    private Long orderId;
    private String productName;
    private Long price;
    private String username;

    // 엔티티에서 DTO로 변환
    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.productName = order.getProductName();
        this.price = order.getProduct().getPrice();
        this.username = order.getUser().getName(); // Lazy Proxy 문제 방지
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProduct().getName(),
                order.getProductPrice(),
                order.getUser().getName()
        );

    }
}