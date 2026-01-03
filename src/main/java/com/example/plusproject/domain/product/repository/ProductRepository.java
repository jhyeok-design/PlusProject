package com.example.plusproject.domain.product.repository;

import com.example.plusproject.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {

    Optional<Product> findByName(String productName);

    boolean existsByName(String name);

    List<Product> findAllByNameContaining(String name);
}
