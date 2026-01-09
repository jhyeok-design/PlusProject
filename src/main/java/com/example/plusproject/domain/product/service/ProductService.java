package com.example.plusproject.domain.product.service;

import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.domain.common.service.S3Service;
import com.example.plusproject.domain.product.entity.Product;
import com.example.plusproject.domain.product.model.ProductDto;
import com.example.plusproject.domain.product.model.request.ProductCreateRequest;
import com.example.plusproject.domain.product.model.request.ProductUpdateRequest;
import com.example.plusproject.domain.product.model.response.ProductCreateResponse;
import com.example.plusproject.domain.product.model.response.ProductReadResponse;
import com.example.plusproject.domain.product.model.response.ProductUpdateResponse;
import com.example.plusproject.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final S3Service s3Service;
    private final ProductRepository productRepository;
    private final ProductCacheService productCacheService;

    /**
     * 상품 생성 - 관리자 권한
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductCreateResponse createProduct(ProductCreateRequest request, MultipartFile image) {

        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 필요합니다.");
        }

        String imageUrl = s3Service.uploadImage(image);

        boolean existsName = productRepository.existsByName(request.getName());

        if (existsName) throw new CustomException(ExceptionCode.EXISTS_PRODUCT_NAME);

        Product product = new Product(request.getName(), request.getPrice(), request.getDescription(), request.getQuantity(), imageUrl);

        Product savedProduct = productRepository.save(product);

        ProductCreateResponse response = ProductCreateResponse.from(ProductDto.from(savedProduct));

        return response;
    }

    /**
     * 상품 단건 조회
     */
    @Transactional(readOnly = true)
    public ProductReadResponse readProduct(Long productId) {

        Product product = readProductIdOrException(productId);

        ProductReadResponse response = ProductReadResponse.from(ProductDto.from(product));

        return response;
    }

    /**
     * 상품명 검색 v2
     */
//    @Cacheable(value = "productCache", key = "'productName :' + #name")
    @Transactional(readOnly = true)
    public List<ProductReadResponse> readProductByName(String name) {

        List<ProductReadResponse> cached = productCacheService.readProductCache(name);

        if (cached != null) {
            return cached;
        }

        List<Product> products = productRepository.findAllByNameContaining(name);

        List<ProductReadResponse> responses = products.stream()
                .map(p -> ProductReadResponse.from(ProductDto.from(p)))
                .toList();

        productCacheService.saveProductCache(name, responses);

        return responses;
    }

    /**
     * 상품 전체 조회
     */
    @Transactional(readOnly = true)
    public List<ProductReadResponse> readAllProduct() {

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(p -> ProductReadResponse.from(ProductDto.from(p)))
                .toList();
    }

    /**
     * 상품 수정 - 관리자 권한
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
     * 상품 삭제 - 관리자 권한
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteProduct(Long productId) {

        Product product = readProductIdOrException(productId);

        product.softDelete();
    }

    /**
     * 상품 여부 확인
     */
    private Product readProductIdOrException(Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_PRODUCT));
    }
}
