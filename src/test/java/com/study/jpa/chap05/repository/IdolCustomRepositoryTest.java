package com.study.jpa.chap05.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.jpa.chap05.dto.GroupAverageAgeDto;
import com.study.jpa.chap05.entity.Group;
import com.study.jpa.chap05.entity.Idol;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.study.jpa.chap05.entity.QIdol.idol;

@SpringBootTest
@Transactional
class IdolCustomRepositoryTest {

    @Autowired
    IdolCustomRepository idolCustomRepository;

    @Autowired
    JPAQueryFactory factory;

    @Autowired
    IdolRepository idolRepository;

    @Autowired
    GroupRepository groupRepository;

    @Test
    void setUp() {
        //given
        Group leSserafim = new Group("르세라핌");
        Group ive = new Group("아이브");
        Group bts = new Group("방탄소년단");
        Group newjeans = new Group("뉴진스");
        groupRepository.save(leSserafim);
        groupRepository.save(ive);
        groupRepository.save(bts);
        groupRepository.save(newjeans);

        Idol idol1 = new Idol("김채원", 24, "여", leSserafim);
        Idol idol2 = new Idol("사쿠라", 26, "여", leSserafim);
        Idol idol3 = new Idol("가을", 22, "여", ive);
        Idol idol4 = new Idol("리즈", 20, "여", ive);
        Idol idol5 = new Idol("장원영", 20, "여", ive);
        Idol idol6 = new Idol("안유진", 21, "여", ive);
        Idol idol7 = new Idol("카즈하", 21, "여", leSserafim);
        Idol idol8 = new Idol("RM", 29, "남", bts);
        Idol idol9 = new Idol("정국", 26, "남", bts);
        Idol idol10 = new Idol("해린", 18, "여", newjeans);
        Idol idol11 = new Idol("혜인", 16, "여", newjeans);
        idolRepository.save(idol1);
        idolRepository.save(idol2);
        idolRepository.save(idol3);
        idolRepository.save(idol4);
        idolRepository.save(idol5);
        idolRepository.save(idol6);
        idolRepository.save(idol7);
        idolRepository.save(idol8);
        idolRepository.save(idol9);
        idolRepository.save(idol10);
        idolRepository.save(idol11);
    }

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

    @Test
    @DisplayName("페이지 처리하기")
    void pagingTest() {
        // given
        int pageNo = 2;
        int pageSize = 2;

        // when
        List<Idol> pageIdol = factory
                .select(idol)
                .from(idol)
                .orderBy(idol.age.desc())
                .offset((pageNo - 1) * pageSize)
                .limit(pageSize)
                .fetch();

        // queryDsl을 사용하면 추가 정보는 따로 찾아야 함
        // 총 데이터 수
        Long cnt = Optional.ofNullable(factory.select(idol.count())
                .from(idol)
                .fetchOne()).orElse(0L);

        // then
        pageIdol.forEach(System.out::println);
        System.out.println(cnt);
    }

    @Test
    @DisplayName("성별, 그룹 별로 그룹화 -> 아이돌 숫자가 3명 이하인 그룹만 조회")
    void groupByTest() {
        List<Tuple> groupBy = factory
                .select(idol.gender, idol.group, idol.count())
                .from(idol)
                .groupBy(idol.gender, idol.group)
                .having(idol.count().loe(3))
                .fetch();

        // then
        for (Tuple tuple : groupBy) {
            String gender = tuple.get(idol.gender);
            Group group = tuple.get(idol.group);
            Long count = tuple.get(idol.count());

            System.out.println(gender + " " + group.getGroupName() + " " + count);
            System.out.println("======================================");
        }
    }

    @Test
    @DisplayName("그룹별 평균 나이 조회")
    void groupAverageAgeTest() {
        /*
            SELECT G.group_name, AVG(I.age)
            FROM tbl_idol I
            JOIN tbl_group G
            ON I.group_id = G.group_id
            GROUP BY G.group_id
            HAVING AVG(I.age) BETWEEN 20 AND 25
         */

        List<Tuple> result = factory
                .select(idol.group.groupName, idol.age.avg())
                .from(idol)
                .groupBy(idol.group)
                .having(idol.age.avg().between(20, 25))
                .fetch();

        for (Tuple tuple : result) {
            String groupName = tuple.get(idol.group.groupName);
            Double averageAge = tuple.get(idol.age.avg());

            System.out.println(groupName + " " + averageAge);

        }
    }

    @Test
    @DisplayName("그룹별 평균 나이 조회")
    void groupAverageAgeDtoTest() {
        /*
            SELECT G.group_name, AVG(I.age)
            FROM tbl_idol I
            JOIN tbl_group G
            ON I.group_id = G.group_id
            GROUP BY G.group_id
            HAVING AVG(I.age) BETWEEN 20 AND 25
         */

        List<GroupAverageAgeDto> result = factory
                .select(
                        Projections.constructor(
                                GroupAverageAgeDto.class,
                                idol.group.groupName,
                                idol.age.avg()

                                )
                )
                .from(idol)
                .groupBy(idol.group)
                .having(idol.age.avg().between(20, 25))
                .fetch();

        for (GroupAverageAgeDto dto : result) {
            String groupName = dto.getGroupName();
            double averageAge = dto.getAverageAge();

            System.out.println(groupName + " " + averageAge);

        }
    }

}