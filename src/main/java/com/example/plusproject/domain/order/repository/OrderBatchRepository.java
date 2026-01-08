package com.example.plusproject.domain.order.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<Object[]> batchArgs) {

        String sql = """
                        INSERT INTO orders
                        (product_name, product_price, total_price, user_id, product_id, is_deleted)
                        VALUES (?, ?, ?, ?, ?, false)
                """;

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
