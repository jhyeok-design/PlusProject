package com.example.plusproject.domain.product.repository;

import com.example.plusproject.domain.product.entity.Product;
//import jakarta.persistence.LockModeType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.jpa.repository.Lock;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.name = :name")
    Optional<Product> findByNameForUpdate(@Param("name") String name);

    boolean existsByName(String name);

    List<Product> findAllByNameContaining(String name);
}
