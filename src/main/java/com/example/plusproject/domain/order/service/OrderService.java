package com.example.plusproject.domain.order.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.domain.order.entity.Order;
import com.example.plusproject.domain.order.model.OrderDto;
import com.example.plusproject.domain.order.model.request.OrderCreateRequest;
import com.example.plusproject.domain.order.model.response.OrderCreateResponse;
import com.example.plusproject.domain.order.model.response.OrderReadResponse;
import com.example.plusproject.domain.order.repository.OrderRepository;
import com.example.plusproject.domain.product.entity.Product;
import com.example.plusproject.domain.product.repository.ProductRepository;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    /**
     * 주문 생성
     */
    @Transactional
    public OrderCreateResponse createOrder(AuthUser authUser, OrderCreateRequest request) {
        // 사용자 조회 / 없으면 예외 처리
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_USER)
        );

        // request의 상품 조회 / 없으면 예외 처리
        Product product = productRepository.findByNameForUpdate(request.getProductName()).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_PRODUCT)
        );

        // 상품 재고 소진시
        product.decreaseQuantity();

        // 주문 생성 및 저장
        Order order = new Order(request.getProductName(), user, product);
        orderRepository.save(order);

        // 반환
        return OrderCreateResponse.from(OrderDto.from(order));
    }


    /**
     * 주문 단건 조회
     */
    @Transactional(readOnly = true)
    public OrderReadResponse readOneOrder(AuthUser authUser, Long orderId) {

        // 사용자 조회 / 없으면 예외 처리
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_USER)
        );

        // 주문건 조회 / 없으면 예외 처리
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_ORDER)
        );

        // 주문건이 해당 사용자의 것이 맞는지 / 아니면 예외 처리
        isOwnedBy(order, user);

        // 응답 값 반환
        return OrderReadResponse.from(OrderDto.from(order));
    }

    // 주문건 - 회원 일치 여부
    private static void isOwnedBy(Order order, User user) {
        if (!order.getUser().getId().equals(user.getId())) {
            throw new CustomException(ExceptionCode.ORDER_ACCESS_DENIED);
        }
    }

    /**
     * 유저의 주문 다건 조회
     */
    @Transactional(readOnly = true)
    public List<OrderReadResponse> readAllOrder(AuthUser authUser) {

        // 사용자 존재 여부 / 예외 처리
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_USER)
        );

        // 해당 유저의 주문 목록 조회
        List<Order> all = orderRepository.findAllWithUser();
        List<OrderReadResponse> responseList = new ArrayList<>();

        for (Order order: all) {

            OrderReadResponse response = OrderReadResponse.from(OrderDto.from(order));

            responseList.add(response);
        }

        return responseList;
    }


    /**
     * 주문 삭제
     */
    @Transactional
    public void deleteOrder(AuthUser authUser, Long orderId) {

        // 사용자 존재 여부 / 예외 처리
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_USER)
        );

        // 주문건 조회 / 없으면 예외 처리
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(ExceptionCode.NOT_FOUND_ORDER)
        );

        // 삭제
        order.softDelete();
    }
}
