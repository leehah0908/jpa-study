package com.study.jpa.chap05.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.jpa.chap05.entity.Idol;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.study.jpa.chap05.entity.QIdol.idol;

// QueryDSL 전용 구현체
@Repository
@RequiredArgsConstructor
public class IdolRepositoryCustomImpl implements IdolCustomRepository {

    private final JPAQueryFactory factory;

    @Override
    public List<Idol> findAllSortedByName() {
        return factory
                .select(idol)
                .from(idol)
                .orderBy(idol.idolName.asc())
                .fetch();
    }

    @Override
    public List<Idol> findByGroupName(String groupName) {
        return factory
                .select(idol)
                .from(idol)
                .where(idol.group.groupName.eq(groupName))
                .fetch();
    }
}
