package com.example.plusproject.domain.product.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.domain.product.entity.Product;
import com.example.plusproject.domain.product.model.ProductDto;
import com.example.plusproject.domain.product.model.request.ProductCreateRequest;
import com.example.plusproject.domain.product.model.request.ProductUpdateRequest;
import com.example.plusproject.domain.product.model.response.ProductCreateResponse;
import com.example.plusproject.domain.product.model.response.ProductReadResponse;
import com.example.plusproject.domain.product.model.response.ProductUpdateResponse;
import com.example.plusproject.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * - 사용자 모두 조회 가능
     */
    @Transactional(readOnly = true)
    public ProductReadResponse readProduct(Long productId) {

        Product product = readProductIdOrException(productId);

        ProductReadResponse response = ProductReadResponse.from(ProductDto.from(product));

        return response;
    }

    /**
     * 상품명 검색 v2
     * - 사용자 모두 조회 가능
     */
    @Cacheable(value = "productCache", key = "'productName :' + #name")
    @Transactional(readOnly = true)
    public List<ProductReadResponse> readProductByName(String name) {

        List<Product> productsByName = productRepository.findAllByNameContaining(name);

        return productsByName.stream()
                .map(p -> ProductReadResponse.from(ProductDto.from(p)))
                .toList();
    }

    /**
     * 상품 전체 조회
     * - 사용자 모두 조회 가능
     */
    @Transactional(readOnly = true)
    public List<ProductReadResponse> readAllProduct() {

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(p -> ProductReadResponse.from(ProductDto.from(p)))
                .toList();
    }

    /**
     * 상품 수정
     * - ADMIN 권한을 가진 사용자만 상품을 수정.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductUpdateResponse updateProduct(ProductUpdateRequest request, Long productId) {

        Product product = readProductIdOrException(productId);

        product.update(request);

        ProductUpdateResponse response = ProductUpdateResponse.from(ProductDto.from(product));

        return response;
    }

    /**
     * 상품 삭제
     * - ADMIN 권한을 가진 사용자만 상품을 삭제.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteProduct(Long productId) {

        Product product = readProductIdOrException(productId);

        product.softDelete();
    }


    private Product readProductIdOrException(Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_PRODUCT));
    }
}
