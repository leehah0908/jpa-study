package com.study.jpa.chap05.repository;

import com.study.jpa.chap05.entity.Idol;

import java.util.List;
import java.util.Optional;

// QueryDSL 레포지토리로 사용할 것 (JPA 상속X)
public interface IdolCustomRepository {

    // 이름으로 오름차해서 전체 조회 (쿼리에서드 양식처럼 써도 JPA 상속안해서 인식 못함)
    List<Idol> findAllSortedByName();

    // 그룹명으로 아이돌 조회
    Optional<List<Idol>> findByGroupName(String groupName);

}
