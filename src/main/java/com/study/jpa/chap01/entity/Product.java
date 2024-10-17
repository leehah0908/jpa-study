package com.study.jpa.chap01.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    @Column(name = "prod_id")
    private Long id;

    @Column(name = "prod_nm", length = 30, nullable = false)
    private String name;

    @Column(name = "prod_price")
    private int price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @CreationTimestamp // insert시 자동으로 시간 저정
    @Column(updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp // upsate시 자동으로 시간 저정
    private LocalDateTime updateTime;

    @Transient // db에는 저장X
    private String nickname;

    public enum Category {
        FOOD, FASHION, ELECTRONIC
    }
}
