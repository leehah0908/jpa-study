package com.study.jpa.chap05.repository;

import com.study.jpa.chap05.entity.Idol;
import org.springframework.data.jpa.repository.JpaRepository;

// JPA용 인터페이스 -> 쿼리 메서드와 JPQL을 작성하는 인터페이스
public interface IdolRepository extends JpaRepository<Idol, Long> {


}
