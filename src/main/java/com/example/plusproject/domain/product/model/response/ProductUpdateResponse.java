package com.example.plusproject.domain.product.model.response;

import com.example.plusproject.domain.product.model.ProductDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductUpdateResponse {

    private final String name;
    private final Long price;
    private final String description;
    private final Long quantity;
    private final LocalDateTime updatedAt;

    public static ProductUpdateResponse from(ProductDto dto) {
        return new ProductUpdateResponse(
                dto.getName(),
                dto.getPrice(),
                dto.getDescription(),
                dto.getQuantity(),
                dto.getUpdatedAt());
    }
}