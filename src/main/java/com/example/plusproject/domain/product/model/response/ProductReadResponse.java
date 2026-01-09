package com.example.plusproject.domain.product.model.response;

import com.example.plusproject.domain.product.model.ProductDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductReadResponse {

    private final Long id;
    private final String name;
    private final Long price;
    private final String description;
    private final Long quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ProductReadResponse from(ProductDto dto) {
        return new ProductReadResponse(
                dto.getId(),
                dto.getName(),
                dto.getPrice(),
                dto.getDescription(),
                dto.getQuantity(),
                dto.getCreatedAt(),
                dto.getUpdatedAt());
    }
}
