package com.study.jpa.chap05.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.jpa.chap05.entity.Group;
import com.study.jpa.chap05.entity.Idol;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.study.jpa.chap05.entity.QIdol.idol;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Rollback(false)
class GroupRepositoryTest {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    IdolRepository idolRepository;

    @Autowired
    JPAQueryFactory factory;

    @Autowired
    EntityManager entityManager;

    @Test
    void setUp() {
        //given
        Group leSserafim = new Group("르세라핌");
        Group ive = new Group("아이브");
        groupRepository.save(leSserafim);
        groupRepository.save(ive);

//        Idol idol1 = new Idol("김채원", 24, leSserafim);
//        Idol idol2 = new Idol("사쿠라", 26, leSserafim);
//        Idol idol3 = new Idol("가을", 22, ive);
//        Idol idol4 = new Idol("리즈", 20, ive);
//        idolRepository.save(idol1);
//        idolRepository.save(idol2);
//        idolRepository.save(idol3);
//        idolRepository.save(idol4);
    }

    @Test
    @DisplayName("JPQL로 특정이름의 아이돌 조회하기")
    void jpqlTest() {
        //given
        String jpqlQuery = "SELECT i FROM Idol i WHERE i.idolName = ?1";

        //when
        Idol foundIdol = entityManager.createQuery(jpqlQuery, Idol.class)
                .setParameter(1, "가을")
                .getSingleResult();

        //then
        assertEquals("아이브", foundIdol.getGroup().getGroupName());
        System.out.println("\n\n\n\n");
        System.out.println("foundIdol = " + foundIdol);
        System.out.println("foundIdol.getGroup() = " + foundIdol.getGroup());
        System.out.println("\n\n\n\n");
    }

    @Test
    @DisplayName("queryDsl 특정 이름의 아이돌 조회")
    void queryDslTest() {
        // given

        // when
        Idol findOne = factory
                .select(idol)
                .from(idol)
                .where(idol.idolName.eq("리즈")
                        .and(idol.group.groupName.eq("아이브")))
                // 조회값이 하나가 아닌 여러개인 경우에는 fetch()를 사용
                // 조회값이 여러개인데 맨 앞에 하나만 가지고 오고 싶을 때는 fetchFirst()를 사용
                .fetchOne();

        // then
        System.out.println("\n\n\n\n");
        System.out.println("foundIdol = " + findOne);
        System.out.println("foundIdol.getGroup() = " + findOne.getGroup().getGroupName());
        System.out.println("\n\n\n\n");

//        idol.idolName.eq("리즈") // idolName = '리즈'
//        idol.idolName.ne("리즈") // idolName != '리즈'
//        idol.idolName.eq("리즈").not() // idolName != '리즈'
//        idol.idolName.isNotNull() //이름이 is not null
//        idol.age.in(10, 20) // age in (10,20)
//        idol.age.notIn(10, 20) // age not in (10, 20)
//        idol.age.between(10,30) //between 10, 30
//        idol.age.goe(30) // age >= 30
//        idol.age.gt(30) // age > 30
//        idol.age.loe(30) // age <= 30
//        idol.age.lt(30) // age < 30
//        idol.idolName.like("_김%")  // like _김%
//        idol.idolName.contains("김") // like %김%
//        idol.idolName.startsWith("김") // like 김%
//        idol.idolName.endsWith("김") // like %김
    }

    @Test
    @DisplayName("Test")
    void teTest() {
        // 이름에 '김'이 포함된 아이돌 조회
        List<Idol> kims = factory.select(idol)
                .from(idol)
                .where(idol.idolName.like("%김%"))
                .fetch();

        // 나이가 20세에서 25세 사이인 아이돌 조회
        List<Idol> two = factory.select(idol)
                .from(idol)
                .where(idol.age.between(20, 25))
                .fetch();

        kims.forEach(System.out::println);
        System.out.println("\n\n\n\n");
        two.forEach(System.out::println);
    }

}