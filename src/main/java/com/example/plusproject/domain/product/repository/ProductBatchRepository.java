package com.example.plusproject.domain.product.repository;

import com.example.plusproject.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void batchInsert(List<Product> productsList) {

        log.info("batchInsert start");

        String sql = "insert into products (name,  price, description, quantity, is_deleted) values (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, productsList, 1000,
                (ps, product) -> {
                    ps.setString(1, product.getName());
                    ps.setLong(2, product.getPrice());
                    ps.setString(3, product.getDescription());
                    ps.setLong(4, product.getQuantity());
                    ps.setBoolean(5, false);
                });

        log.info("BatchInsert end");
    }
}
