package com.example.plusproject.domain.order.repository;

import com.example.plusproject.common.util.PasswordEncoder;
import com.example.plusproject.domain.user.entity.User;
import com.example.plusproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


@SpringBootTest
//@Disabled("대용량 주문 데이터 생성용 테스트")
@Commit
public class OrderBatchRepositoryTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OrderBatchRepository orderBatchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int TOTAL_ORDERS = 5_000_000;
    private static final int BATCH_SIZE = 1_000;

    @BeforeEach
    void setUp() {
//        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("DELETE FROM orders");
//        jdbcTemplate.execute("DELETE FROM users");

        // 유저 10명 새로 생성
        for (int i = 1; i <= 10; i++) {
            User user = new User(
                    "user" + i,
                    "user" + i + "@example.com",
                    passwordEncoder.encode("password"), // 암호화해서 저장
                    "nickname" + i,
                    "0100000000" + i,
                    "서울시 강남구"
            );
            userRepository.save(user);
        }
    }

    @Transactional
    @Test
    void bulkInsert_orders() {

        // 상품 id, name, price만 가져오기
        List<Map<String, Object>> products = jdbcTemplate.queryForList(
                "SELECT id, name, price FROM products LIMIT 50000"
        );

        // DB에 있는 유저 id 가져오기
        List<Long> userIds = jdbcTemplate.queryForList("SELECT id FROM users", Long.class);

        Random random = new Random();
        List<Object[]> batch = new ArrayList<>(BATCH_SIZE);

        long start = System.currentTimeMillis();
        int count = 0;

        for (int i = 0; i < TOTAL_ORDERS; i++) {

            Map<String, Object> product = products.get(random.nextInt(products.size()));

            long productId = ((Number) product.get("id")).longValue();
            String productName = (String) product.get("name");
            long price = ((Number) product.get("price")).longValue();
            Long userId = userIds.get(random.nextInt(userIds.size()));

            batch.add(new Object[]{
                    productName,
                    price,
                    price,
                    userId,
                    productId
            });

            if (batch.size() == BATCH_SIZE) {
                orderBatchRepository.batchInsert(batch);
                count += batch.size();
                batch.clear();

                if (count % 100_000 == 0) {
                    System.out.println("Inserted orders: " + count);
                }
            }
        }

        if (!batch.isEmpty()) {
            orderBatchRepository.batchInsert(batch);
        }

        long end = System.currentTimeMillis();
        System.out.println("총 소요 시간(ms): " + (end - start));
    }
}