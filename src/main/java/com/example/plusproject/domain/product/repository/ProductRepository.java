package com.example.plusproject.domain.product.repository;

import com.example.plusproject.domain.product.entity.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(@NotBlank(message = "상품명을 입력해주세요") String productName);

    boolean existsByName(String name);
}
