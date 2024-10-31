package com.study.jpa.chap05.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.jpa.chap05.entity.Idol;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.study.jpa.chap05.entity.QIdol.idol;

@SpringBootTest
@Transactional
public class QueryDslDynamicTest {

    @Autowired
    JPAQueryFactory factory;

    @Test
    @DisplayName("동적 쿼리를 사용한 아이돌 조회")
    void dynamicTest1() {

        String name = "안유진";
        String gender = "여";

        // 동적 쿼리를 위한 BooleanBuilder
        BooleanBuilder bb = new BooleanBuilder();

        if(name != null){
            bb.and(idol.idolName.eq(name));
        }

        if(gender != null){
            bb.or(idol.gender.eq(gender));
        }

        // when
        List<Idol> result = factory
                .selectFrom(idol)
                .where(bb)
                .fetch();

        // then
        System.out.println(result);
    }

    @Test
    @DisplayName("동적 쿼리를 사용한 아이돌 조회")
    void dynamicTest2() {

        String name = "안유진";
        String gender = "여";

        // when
        List<Idol> result = factory
                .selectFrom(idol)
                .where(nameEq(name), genderEq(gender))
                .fetch();

        // then
        System.out.println(result);
    }

    // 조건이 전달되지 않으면 건너뜀
    private BooleanExpression nameEq(String name) {
        return name != null ? idol.idolName.eq(name) : null;
    }

    private BooleanExpression genderEq(String gender) {
        return gender != null ? idol.gender.eq(gender) : null;
    }


    @Test
    @DisplayName("동적 정렬을 사용한 아이돌 조회")
    void dynamicTest3() {
        // given
        String sortBy = "age";
        boolean asc = true; // 오름차순

        OrderSpecifier<?> os = null;

        // 동적 정렬 조건 생성
        switch (sortBy) {
            case "idolName":
                os = asc? idol.idolName.asc() : idol.idolName.desc();
                break;

            case "groupName":
                os = asc? idol.group.groupName.asc() : idol.group.groupName.desc();
                break;

            case "age":
                os = asc ? idol.age.asc() : idol.age.desc();
                break;

        }

        // when
        List<Idol> result = factory
                .selectFrom(idol)
                .orderBy(os)
                .fetch();

        // then
        result.forEach(System.out::println);

    }
}
