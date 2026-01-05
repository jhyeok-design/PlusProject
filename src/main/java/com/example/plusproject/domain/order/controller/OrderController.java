package com.example.plusproject.domain.order.controller;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.order.entity.Order;
import com.example.plusproject.domain.order.model.request.OrderCreateRequest;
import com.example.plusproject.domain.order.model.response.OrderCreateResponse;
import com.example.plusproject.domain.order.model.response.OrderPageResponse;
import com.example.plusproject.domain.order.model.response.OrderReadResponse;
import com.example.plusproject.domain.order.service.OrderService;
import com.example.plusproject.domain.search.service.SearchService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final SearchService searchService;

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


    /**
     * 검색 - v1
     */
    @GetMapping("/searchV1")
    public ResponseEntity<CommonResponse<Page<OrderPageResponse>>> searchV1(
            @RequestParam String keyword,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<OrderPageResponse> response = orderService.searchV1(keyword, pageable);

        return ResponseEntity.ok(CommonResponse.success("주문 검색 - V1 조회 성공", response));
    }


    /**
     * 검색 - v2
     */
    @GetMapping("/searchV2")
    public ResponseEntity<CommonResponse<Page<OrderPageResponse>>> searchV2(
            @RequestParam String keyword,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<OrderPageResponse> response = orderService.searchV2(keyword, pageable);

        return ResponseEntity.ok(CommonResponse.success("주문 검색 - V2 조회 성공", response));
    }

}
