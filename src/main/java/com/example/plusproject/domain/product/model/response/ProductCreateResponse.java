package com.example.plusproject.domain.product.model.response;

import com.example.plusproject.domain.product.model.ProductDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductCreateResponse {

    private final Long id;
    private final String name;
    private final Long price;
    private final String description;
    private final Long quantity;
    private final LocalDateTime createdAt;

    public static ProductCreateResponse from(ProductDto dto){
        return new ProductCreateResponse(
                dto.getId(),
                dto.getName(),
                dto.getPrice(),
                dto.getDescription(),
                dto.getQuantity(),
                dto.getCreatedAt());
    }
}