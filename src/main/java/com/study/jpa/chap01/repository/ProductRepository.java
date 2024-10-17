package com.study.jpa.chap01.repository;

import com.study.jpa.chap01.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository<엔티티 클래스 타입, PK의 타입>
public interface ProductRepository extends JpaRepository<Product, Long> {
    void deleteByName(String name);
    Optional<Product> findByName(String name);
}
