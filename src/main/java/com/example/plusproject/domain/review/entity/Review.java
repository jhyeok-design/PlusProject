package com.example.plusproject.domain.review.entity;

import com.example.plusproject.common.entity.BaseEntity;
import com.example.plusproject.domain.product.entity.Product;
import com.example.plusproject.domain.review.model.request.ReviewUpdateRequest;
import com.example.plusproject.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 1)
    @Max(5)
    private Integer score;

    public Review(User user, Product product, String content, Integer score) {
        this.user = user;
        this.product = product;
        this.content = content;
        this.score = score;
    }

    public void update(ReviewUpdateRequest request) {
        this.content = (!request.getContent().isBlank()) ? request.getContent() : this.content;
        this.score = (request.getScore() != null) ? request.getScore() : this.score;
    }
}
