package com.example.plusproject.domain.order.repository;

import com.example.plusproject.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderCustomRepository {

    Page<Order> findAllWithUser(Pageable pageable);
}
