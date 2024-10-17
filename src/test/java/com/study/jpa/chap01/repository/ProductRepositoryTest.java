package com.study.jpa.chap01.repository;

import com.study.jpa.chap01.entity.Product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(false)
class ProductRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("상품 저장")
    void saveTest() {
        // given
        Product p1 = Product.builder()
                .name("신발")
                .price(90000)
                .category(Product.Category.FASHION)
                .build();

        // when
        entityManager.persist(p1);

        // then
    }

    @Test
    @DisplayName("저장된 상품 조회")
    void findAllTest() {
        // given
        Long id = 1L;

        // when
        Product product = entityManager.find(Product.class, id);

        // then
        Assertions.assertEquals(id, product.getId());
    }

    @Test
    @DisplayName("영속성 컨택스트의 1차 캐시")
    void persistTest() {
        // given
        Product p2 = Product.builder()
                .name("탕수육")
                .price(18000)
                .category(Product.Category.FOOD)
                .build();

        // when
        entityManager.persist(p2);
        Product product = entityManager.find(Product.class, 2L);

        // then
        Assertions.assertEquals(18000, product.getPrice());
    }

    @Test
    @DisplayName("상품 가격 수정")
    void updateTest() {
        // given
        Product product = entityManager.find(Product.class, 1L);

        // when
        product.setPrice(50000);
        product.setName("할인된 신밡");

        entityManager.persist(product);

        // then
        Assertions.assertEquals(50000, entityManager.find(Product.class, product.getId()).getPrice());
    }

}