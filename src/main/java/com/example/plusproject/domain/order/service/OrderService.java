package com.example.plusproject.domain.order.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.domain.order.entity.Order;
import com.example.plusproject.domain.order.model.OrderDto;
import com.example.plusproject.domain.order.model.request.OrderCreateRequest;
import com.example.plusproject.domain.order.model.response.OrderCreateResponse;
import com.example.plusproject.domain.order.model.response.OrderPageResponse;
import com.example.plusproject.domain.order.model.response.OrderReadResponse;
import com.example.plusproject.domain.order.model.response.OrderResponse;
import com.example.plusproject.domain.order.repository.OrderRepository;
import com.example.plusproject.domain.product.entity.Product;
import com.example.plusproject.domain.product.repository.ProductRepository;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderCacheService orderCacheService;

    /**
     * 주문 생성
     */
    @Transactional
    public OrderCreateResponse createOrder(AuthUser authUser, OrderCreateRequest request) {

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER)
                );

        Product product = productRepository.findByNameForUpdate(request.getProductName())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_PRODUCT)
                );

        product.decreaseQuantity();

        Order order = new Order(request.getProductName(), user, product);

        orderRepository.save(order);

        return OrderCreateResponse.from(OrderDto.from(order));
    }


    /**
     * 주문 단건 조회
     */
    @Transactional(readOnly = true)
    public OrderReadResponse readOneOrder(AuthUser authUser, Long orderId) {

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER)
                );

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ORDER)
                );

        validOwnedBy(order, user);

        return OrderReadResponse.from(OrderDto.from(order));
    }

    /**
     * 회원 일치 여부 확인
     */
    private static void validOwnedBy(Order order, User user) {
        if (!order.getUser().getId().equals(user.getId())) {
            throw new CustomException(ExceptionCode.ORDER_ACCESS_DENIED);
        }
    }

    /**
     * 유저의 주문 다건 조회
     */
    @Transactional(readOnly = true)
    public List<OrderReadResponse> readAllOrder(AuthUser authUser) {

        userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER)
                );

        List<Order> all = orderRepository.findAllWithUser();

        List<OrderReadResponse> responseList = new ArrayList<>();

        for (Order order : all) {
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

        userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER)
                );

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ORDER)
                );

        order.softDelete();
    }

//    /**
//     * 주문 검색 - v1
//     */
//    @Transactional(readOnly = true)
//    public OrderPageResponse<OrderResponse> searchV1(String keyword, Pageable pageable) {
//
//        Page<Order> orderPage = orderRepository.findAllByProduct_NameContaining(keyword, pageable);
//
//        Page<OrderResponse> orderResponsePage = orderPage.map(OrderResponse::from);
//
//        return new OrderPageResponse<>(orderResponsePage);
//    }

    /**
     * 주문 검색 - v2 (In-memory Cache)
     */
    @Cacheable(value = "orderCache",
            key = "'keyword: ' + #keyword + ':page:' + #pageable.pageNumber",
            condition = "#pageable.pageNumber == 0")
    @Transactional(readOnly = true)
    public Page<OrderResponse> searchV2(String keyword, Pageable pageable) {

        log.info("Redis Cache Miss {} ", keyword);

        Page<Order> orderPage = orderRepository.findAllByProduct_NameContaining(keyword, pageable);

        return orderPage.map(OrderResponse::from);
    }

//    /**
//     * 주문 검색 - v3 (Redis 를 이용한 Remote Cache)
//     */
//    @Transactional(readOnly = true)
//    public OrderPageResponse<OrderResponse> searchV3(String keyword, Pageable pageable) {
//
//        int page = pageable.getPageNumber();
//
//        int size = pageable.getPageSize();
//
//        OrderPageResponse<OrderResponse> cached = orderCacheService.getOrderCache(keyword, page, size);
//
//        if (cached != null) {
//            log.info("Redis Cache Hit keyword={}, page={}, size={}", keyword, page, size);
//            return cached;
//        }
//
//        log.info("Redis Cache Miss keyword={}, page={}, size={}", keyword, page, size);
//
//        Page<Order> orderPage = orderRepository.findAllByProduct_NameContaining(keyword, pageable);
//
//        List<OrderResponse> content =
//                orderPage.getContent()
//                        .stream()
//                        .map(OrderResponse::from)
//                        .toList();
//
//        OrderPageResponse<OrderResponse> response =
//                new OrderPageResponse<>(
//                        content,
//                        orderPage.getNumber(),
//                        orderPage.getSize(),
//                        orderPage.getTotalElements(),
//                        orderPage.getTotalPages()
//                );
//
//        orderCacheService.saveOrderCache(keyword, page, size, response);
//
//        return response;
//    }
}