package com.example.plusproject.domain.product.entity;

import com.example.plusproject.common.entity.BaseEntity;
import com.example.plusproject.common.enums.ExceptionCode;
import com.example.plusproject.common.exception.CustomException;
import com.example.plusproject.domain.product.model.request.ProductUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public Product(String name, Long price, String description, Long quantity) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }

    public void update(ProductUpdateRequest request) {
        this.name = request.getName() != null ? request.getName() : this.name;
        this.price = request.getPrice() != null ? request.getPrice() : this.price;
        this.description = request.getDescription() != null ? request.getDescription() : this.description;
        this.quantity = request.getQuantity() != null ? request.getQuantity() : this.quantity;
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    public void decreaseQuantity() {
        if (this.quantity <= 0) {
            throw new CustomException(ExceptionCode.OUT_OF_STOCK);
        }
        this.quantity--;
    }
}