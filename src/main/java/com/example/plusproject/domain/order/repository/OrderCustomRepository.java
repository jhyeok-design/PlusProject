package com.example.plusproject.domain.order.repository;

import com.example.plusproject.domain.order.entity.Order;

import java.util.List;

public interface OrderCustomRepository {

    List<Order> findAllWithUser();
}
