package com.example.plusproject.domain.order.service;

import com.example.plusproject.common.model.AuthUser;
import com.example.plusproject.domain.order.model.request.OrderCreateRequest;
import com.example.plusproject.domain.order.repository.OrderRepository;
import com.example.plusproject.domain.product.entity.Product;
import com.example.plusproject.domain.product.repository.ProductRepository;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderServicePessimisticLockTest {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void 주문_동시성_테스트_비관적락() throws Exception {

        User user = userRepository.save(new User("홍길동", "test@test.com", "pw", "nick", "010", "서울"));
        Product product = productRepository.save(new Product("돌맹이", 10000L, "귀여움", 1L));

        AuthUser authUser = new AuthUser(user.getId(), user.getRole());
        OrderCreateRequest request = new OrderCreateRequest("돌맹이");

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    orderService.createOrder(authUser, request);
                    success.incrementAndGet();
                } catch (Exception e) {
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product result = productRepository.findById(product.getId()).orElseThrow();

        System.out.println("성공 주문 수 = " + success.get());
        System.out.println("실패 주문 수 = " + fail.get());
        System.out.println("남은 재고 = " + result.getQuantity());

        assertThat(success.get()).isEqualTo(1);
        assertThat(fail.get()).isEqualTo(9);
        assertThat(result.getQuantity()).isEqualTo(0);
        assertThat(orderRepository.count()).isEqualTo(1);
    }
}
