package com.study.jpa.chap05.repository;

import com.study.jpa.chap05.entity.Idol;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class IdolCustomRepositoryTest {

    @Autowired
    IdolCustomRepository idolCustomRepository;

    @Test
    @DisplayName("QueryDSL 커스텀 테스트")
    void testCustom() {
        // given

        // when
        List<Idol> allSortedByName = idolCustomRepository.findAllSortedByName();
        List<Idol> group = idolCustomRepository.findByGroupName("아이브").orElseThrow();

        // then
        System.out.println("\n\n\n");
        allSortedByName.forEach(System.out::println);
        System.out.println("\n\n\n");
        group.forEach(System.out::println);
    }

}