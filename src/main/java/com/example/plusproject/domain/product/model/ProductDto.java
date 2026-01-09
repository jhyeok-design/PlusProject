package com.example.plusproject.domain.product.model;

import com.example.plusproject.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductDto {

    private final Long id;
    private final String name;
    private final Long price;
    private final String description;
    private final Long quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ProductDto from(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }
}