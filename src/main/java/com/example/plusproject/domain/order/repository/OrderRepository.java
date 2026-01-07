package com.example.plusproject.domain.order.repository;

import com.example.plusproject.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {

    // v2, v3
    Page<Order> findAllByProduct_NameContaining(String keyword, Pageable pageable);

}
