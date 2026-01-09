package com.example.plusproject.domain.order.controller;

import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.order.model.request.OrderCreateRequest;
import com.example.plusproject.domain.order.model.response.OrderCreateResponse;
import com.example.plusproject.domain.order.model.response.OrderPageResponse;
import com.example.plusproject.domain.order.model.response.OrderReadResponse;
import com.example.plusproject.domain.order.model.response.OrderResponse;
import com.example.plusproject.domain.order.service.OrderService;
import com.example.plusproject.domain.search.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<CommonResponse<OrderCreateResponse>> createOrder(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody OrderCreateRequest request) {

        OrderCreateResponse response = orderService.createOrder(authUser, request);

        return ResponseEntity.ok(CommonResponse.success("주문 생성 성공", response));
    }

    /**
     * 주문 상세 조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<CommonResponse<OrderReadResponse>> readOneOrder(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long orderId) {

        OrderReadResponse response = orderService.readOneOrder(authUser, orderId);

        return ResponseEntity.ok(CommonResponse.success("주문 상세 조회 성공", response));
    }

    /**
     * 유저의 주문 다건 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse<Page<OrderReadResponse>>> readAllOrder(@AuthenticationPrincipal AuthUser authUser, @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<OrderReadResponse> responseList = orderService.readAllOrder(authUser, pageable);

        return ResponseEntity.ok(CommonResponse.success("주문 다건 조회 성공", responseList));
    }

    /**
     * 주문 삭제
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<CommonResponse<Void>> deleteOrder(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long orderId) {

        orderService.deleteOrder(authUser, orderId);

        return ResponseEntity.ok(CommonResponse.success("주문 삭제 성공", null));
    }


//    /**
//     * 주문 검색 - v1
//     */
//    @GetMapping("/searchV1")
//    public ResponseEntity<CommonResponse<OrderPageResponse<OrderResponse>>> searchV1(@RequestParam String keyword, @PageableDefault(page = 0, size = 10) Pageable pageable) {
//
//        OrderPageResponse<OrderResponse> response = orderService.searchV1(keyword, pageable);
//
//        return ResponseEntity.ok(CommonResponse.success("주문 검색 - V1 조회 성공", response));
//    }

    /**
     * 주문 검색 - v2 (In-memory Cache)
     */
    @GetMapping("/searchV2")
    public ResponseEntity<CommonResponse<Page<OrderResponse>>> searchV2(@RequestParam String keyword, @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<OrderResponse> response = orderService.searchV2(keyword, pageable);

        return ResponseEntity.ok(CommonResponse.success("주문 검색 - V2 조회 성공", response));
    }

//    /**
//     * 주문 검색 - v3 (Redis 를 이용한 Remote Cache)
//     */
//    @GetMapping("/searchV3")
//    public ResponseEntity<CommonResponse<OrderPageResponse<OrderResponse>>> searchV3(@RequestParam String keyword, Pageable pageable) {
//
//        OrderPageResponse<OrderResponse> response = orderService.searchV3(keyword, pageable);
//
//        return ResponseEntity.ok(CommonResponse.success("주문 검색 - V3 조회 성공", response));
//    }
}