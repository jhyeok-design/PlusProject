package com.example.plusproject.domain.order.repository;

import com.example.plusproject.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {
    Page<Order> findAllByProduct_NameContaining(String keyword, Pageable pageable);

//    Page<Order> findAllByUser_IdAndProduct_NameContaining(Long userId, String keyword, Pageable pageable);

}
