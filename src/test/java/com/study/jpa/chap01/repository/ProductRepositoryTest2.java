package com.study.jpa.chap01.repository;

import com.study.jpa.chap01.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.study.jpa.chap01.entity.Product.Category.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class ProductRepositoryTest2 {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void insertBeforeTest() {
        Product p1 = Product.builder()
                .name("아이폰")
                .category(ELECTRONIC)
                .price(2000000)
                .build();
        Product p2 = Product.builder()
                .name("탕수육")
                .category(FOOD)
                .price(20000)
                .build();
        Product p3 = Product.builder()
                .name("구두")
                .category(FASHION)
                .price(300000)
                .build();
        Product p4 = Product.builder()
                .name("주먹밥")
                .category(FOOD)
                .price(1500)
                .build();
        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        productRepository.save(p4);
    }

    @Test
    @DisplayName("상품을 데이터베이스에 저장")
    void insertTest() {
        // given
        Product p1 = Product.builder()
                .name("신발")
                .price(90000)
                .category(Product.Category.FASHION)
                .build();

        // when
        Product saved = productRepository.save(p1);

        // then
        assertNotNull(saved);
    }

    @Test
    @DisplayName("1번 상품 삭제")
    void deleteTest() {
        // given
        Long id = 1L;

        // when
        productRepository.deleteById(id);

        /*
            Optional: Java 8버전 이후에 사용이 가능
            객체의 null값을 검증할 수 있도록 여러가지 기능을 제공하는 타입. (NPE 방지)
        */
        Optional<Product> optionalProduct = productRepository.findById(id);

        // Chaining 방식
        // true or false로 받고 싶을 때
        boolean present = productRepository.findById(id).isPresent();

        // (새로운) 객체로 받고 싶을 때
        Product deleteProduct = productRepository.findById(id).orElse(new Product());

        // 예외로 처리하고 싶을 때
//        productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("값이 없습니다"));

        // 값이 존재하면 후속 동작을 정의
        productRepository.findById(id).ifPresent(p -> System.out.println(p.getName()));

        // then
        assertFalse(present);
    }

    @Test
    @DisplayName("상품 전체 조회를 하면 갯수는 3개여야 함")
    void selectAllTest() {
        // given

        // when
        List<Product> all = productRepository.findAll();

        // then
        assertEquals(3, all.size());
    }

    @Test
    @DisplayName("2번 상품의 이름과 가격 변경")
    void updateTest() {
        // given
        Long id = 2L;
        String newName = "마라탕";
        int newPrice = 10000;

        // when
        productRepository.findById(id).ifPresent(p -> {
            p.setName(newName);
            p.setPrice(newPrice);

            // jpa는 따로 update 메서드를 제공하지 않음
            // 조회한 객체의 필드는 setter로 변경하면 자동(변경이 감지되면)으로 update가 됨
            productRepository.save(p);
        });

        // then

    }
}