package com.study.jpa.chap05.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.jpa.chap05.entity.Idol;
import com.study.jpa.chap05.entity.QAlbum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.study.jpa.chap05.entity.QGroup.group;
import static com.study.jpa.chap05.entity.QIdol.idol;

@SpringBootTest
@Transactional
@Rollback(false)
public class QueryDslSubqueryTest {

    @Autowired
    IdolRepository idolRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    JPAQueryFactory factory;

    @Test
    @DisplayName("특정 그룹의 평균 나이보다 만ㄹ은 아이돌 조회")
    void subQueryTest1() {
        // given
        String groupName = "르세라핌";

        // when
        List<Idol> result = factory
                .select(idol)
                .from(idol)
                .where(idol.age.gt(
                        JPAExpressions
                                .select(idol.age.avg())
                                .from(idol)
                                .where(idol.group.groupName.eq(groupName))
                ))
                .fetch();

        // then
        for (Idol idol1 : result) {
            System.out.println(idol1);
        }
    }

    @Test
    @DisplayName("그룹별 가장 최근의 발매된 앨범 정보 조회")
    void subQueryTest2() {
        /*
            SELECT G.group_name, A.album_name, A.release_year
            FROM tbl_group G
            INNER JOIN tbl_album A
            ON G.group_id = A.group_id
            WHERE A.album_id IN (
                    SELECT S.album_id
                    FROM tbl_album S
                    WHERE S.group_id = A.group_id
                        AND (
                            SELECT MAX(release_year)
                            FROM tbl_album
                            WHERE S.group_id = A.group_id
                        )
            )
         */

        // given
        QAlbum albumA = new QAlbum("albumA");
        QAlbum albumS = new QAlbum("albumS");

        // when
        List<Tuple> result = factory
                .select(group.groupName, albumA.albumName, albumA.releaseYear)
                .from(group)
                .innerJoin(group.albums, albumA)
                .where(albumA.id.in(
                                JPAExpressions
                                        .select(albumS.id)
                                        .from(albumS)
                                        .where(albumS.group.id.eq(albumA.group.id)
                                                .and(albumS.releaseYear.eq(
                                                                JPAExpressions
                                                                        .select(albumS.releaseYear.max())
                                                                        .from(albumS)
                                                                        .where(albumS.group.id.eq(albumA.group.id))

                                                        )
                                                )
                                        )
                        )
                )
                .distinct()
                .fetch();

        // then
        for (Tuple tuple : result) {
            String groupName = tuple.get(group.groupName);
            String albumName = tuple.get(albumA.albumName);
            Integer releaseYear = tuple.get(albumA.releaseYear);
            System.out.println("groupName = " + groupName);
            System.out.println("albumName = " + albumName);
            System.out.println("releaseYear = " + releaseYear);
            System.out.println("\n\n");
        }

    }

    @Test
    @DisplayName("그룹이 존재하지 않는 아이돌 조회")
    void subQueryTest3() {
        // 서브 쿼리: 아이들이 특정 그룹에 속해있는지 확인
        JPQLQuery<Long> subQuery = JPAExpressions
                .select(group.id)
                .from(group)
                .where(group.id.eq(idol.group.id));

        // 메인 쿼리: 서브 쿼리 결과가 존재하지 않는 아이돌 조회
        List<Idol> result = factory
                .selectFrom(idol)
                .where(subQuery.notExists())
                .fetch();


        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("아이브의 평균 나이보다 나이가 많은 여자 이이돌 조회")
    void subQueryTest4() {

        JPQLQuery<Double> subQuery1 = JPAExpressions
                .select(idol.age.avg())
                .from(idol)
                .where(idol.group.groupName.eq("아이브"));

        List<Idol> result = factory
                .selectFrom(idol)
                .where(idol.age.gt(subQuery1).and(idol.gender.eq("여")))
                .fetch();

        result.forEach(System.out::println);
    }

}
