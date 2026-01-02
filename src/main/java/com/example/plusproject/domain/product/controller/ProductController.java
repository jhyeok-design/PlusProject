package com.example.plusproject.domain.product.controller;

import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.product.model.request.ProductCreateRequest;
import com.example.plusproject.domain.product.model.response.ProductCreateResponse;
import com.example.plusproject.domain.product.model.response.ProductReadResponse;
import com.example.plusproject.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 생성
     */
    @PostMapping
    public ResponseEntity<CommonResponse<ProductCreateResponse>> createProduct(@Valid @RequestBody ProductCreateRequest request){

        ProductCreateResponse response = productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("상품 생성 성공", response));
    }

    /**
     * 상품 단건 조회
     */
    @GetMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductReadResponse>> readProduct(@PathVariable Long productId){

        ProductReadResponse response = productService.readProduct(productId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품 단건 조회 성공",response));
    }
}
