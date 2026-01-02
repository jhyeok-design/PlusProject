package com.example.plusproject.domain.product.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.domain.product.entity.Product;
import com.example.plusproject.domain.product.model.ProductDto;
import com.example.plusproject.domain.product.model.request.ProductCreateRequest;
import com.example.plusproject.domain.product.model.response.ProductCreateResponse;
import com.example.plusproject.domain.product.model.response.ProductReadResponse;
import com.example.plusproject.domain.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 상품 생성
     * - ADMIN 권한을 가진 사용자만 상품을 생성.
     * - 상품명은 중복될 수 없음.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request) {

        boolean existsName = productRepository.existsByName(request.getName());

        if (existsName) throw new CustomException(ExceptionCode.EXISTS_PRODUCT_NAME);

        Product product = new Product(request.getName(), request.getPrice(), request.getDescription(), request.getQuantity());

        Product savedProduct = productRepository.save(product);

        ProductCreateResponse response = ProductCreateResponse.from(ProductDto.from(savedProduct));

        return response;

    }


    /**
     * 상품 단건 조회
     * - 사용자/비로그인 모두 조회 가능
     */
    @Transactional(readOnly = true)
    public ProductReadResponse readProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_PRODUCT));

        ProductReadResponse response = ProductReadResponse.from(ProductDto.from(product));

        return response;
    }
}
