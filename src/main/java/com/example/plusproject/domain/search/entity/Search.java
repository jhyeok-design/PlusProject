package com.example.plusproject.domain.search.entity;

import com.example.plusproject.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Entity
@Table(name = "searches")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Search extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String keyword;

    @Column(nullable = false)
    private Long count = 0L;

    public Search(String keyword) {
        this.keyword = keyword;
    }

    public void increaseCount() {
        this.count++;
    }
}
