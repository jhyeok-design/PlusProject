package com.example.plusproject.domain.product.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequest {

    @NotBlank
    private String name;
    @NotNull
    private Long price;
    @NotBlank
    private String description;
    @NotNull
    private Long quantity;
}
