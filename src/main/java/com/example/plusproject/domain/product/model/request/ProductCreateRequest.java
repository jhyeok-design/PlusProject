package com.example.plusproject.domain.product.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "가격을 입력해주세요.")
    private Long price;

    @NotBlank(message = "설명을 입력해주세요.")
    private String description;

    @NotNull(message = "수량을 입력해주세요.")
    private Long quantity;
}
