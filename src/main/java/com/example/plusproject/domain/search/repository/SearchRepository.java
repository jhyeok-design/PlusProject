package com.example.plusproject.domain.search.repository;

import com.example.plusproject.domain.search.entity.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, Long> {
    Optional<Search> findByKeyword(String keyword);

    Page<Search> findAllByOrderByCountDesc(Pageable pageable);
}
