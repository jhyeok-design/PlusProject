package com.example.plusproject.domain.order.controller;

import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.order.model.request.OrderCreateRequest;
import com.example.plusproject.domain.order.model.response.OrderCreateResponse;
import com.example.plusproject.domain.order.model.response.OrderReadResponse;
import com.example.plusproject.domain.order.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성
     */
    @PostMapping
    public ResponseEntity<CommonResponse<OrderCreateResponse>> createOrder(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody OrderCreateRequest request
    ) {
        OrderCreateResponse response = orderService.createOrder(authUser, request);

        return ResponseEntity.ok(CommonResponse.success("주문 생성 성공", response));
    }


    /**
     * 주문 상세 조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<CommonResponse<OrderReadResponse>> readOneOrder(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long orderId
    ) {
        OrderReadResponse response = orderService.readOneOrder(authUser, orderId);

        return ResponseEntity.ok(CommonResponse.success("주문 상세 조회 성공", response));
    }


    /**
     * 유저의 주문 다건 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse<List<OrderReadResponse>>> readAllOrder(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        List<OrderReadResponse> responseList = orderService.readAllOrder(authUser);

        return ResponseEntity.ok(CommonResponse.success("주문 다건 조회 성공", responseList));
    }


    /**
     * 주문 삭제
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<CommonResponse<Void>> deleteOrder(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long orderId
    ) {

        orderService.deleteOrder(authUser, orderId);

        return ResponseEntity.ok(CommonResponse.success("주문 삭제 성공", null));
    }

}
