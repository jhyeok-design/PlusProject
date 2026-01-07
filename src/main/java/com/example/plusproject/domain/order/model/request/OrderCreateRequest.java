package com.example.plusproject.domain.order.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {

    @NotBlank(message = "상품명을 입력해주세요")
    private String productName;
}
