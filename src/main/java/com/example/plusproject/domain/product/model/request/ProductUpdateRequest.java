package com.example.plusproject.domain.product.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequest {

    private String name;
    private Long price;
    private String description;
    private Long quantity;
}