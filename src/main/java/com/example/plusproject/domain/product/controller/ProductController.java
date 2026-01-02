package com.example.plusproject.domain.product.controller;

import com.example.plusproject.common.model.CommonResponse;
import com.example.plusproject.domain.product.model.request.ProductCreateRequest;
import com.example.plusproject.domain.product.model.request.ProductUpdateRequest;
import com.example.plusproject.domain.product.model.response.ProductCreateResponse;
import com.example.plusproject.domain.product.model.response.ProductReadResponse;
import com.example.plusproject.domain.product.model.response.ProductUpdateResponse;
import com.example.plusproject.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 생성
     */
    @PostMapping
    public ResponseEntity<CommonResponse<ProductCreateResponse>> createProduct(@Valid @RequestBody ProductCreateRequest request) {

        ProductCreateResponse response = productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success("상품 생성 성공", response));
    }

    /**
     * 상품 단건 조회
     */
    @GetMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductReadResponse>> readProduct(@PathVariable Long productId) {

        ProductReadResponse response = productService.readProduct(productId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품 단건 조회 성공", response));
    }

    /**
     * 상품명 단건 조회 v2
     */
    @GetMapping("/name")
    public ResponseEntity<CommonResponse<ProductReadResponse>> readProductByName(@RequestParam("name") String name) {

        ProductReadResponse response = productService.readProductByName(name);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품명 단건 조회 성공", response));
    }

    /**
     * 상품 전체 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse<List<ProductReadResponse>>> readAllProduct() {

        List<ProductReadResponse> response = productService.readAllProduct();

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품 전체 조회 성공", response));
    }

    /**
     * 상품 수정
     */
    @PatchMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductUpdateResponse>> updateProduct(@Valid @RequestBody ProductUpdateRequest request, @PathVariable Long productId) {

        ProductUpdateResponse response = productService.updateProduct(request, productId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("상품 수정 성공", response));
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResponse<Void>> deleteProduct(@PathVariable Long productId) {

        productService.deleteProduct(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(CommonResponse.success("상품 삭제 성공", null));
    }

}
