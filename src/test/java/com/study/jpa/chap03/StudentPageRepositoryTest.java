package com.study.jpa.chap03;

import com.study.jpa.chap02.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class StudentPageRepositoryTest {

    @Autowired
    StudentPageRepository repository;

    @BeforeEach
    void bulkInsert() {
        for (int i = 1; i <= 147; i++) {
            Student s = Student.builder()
                    .name("김시골" + i)
                    .city("도시" + i)
                    .major("숨쉬기" + i)
                    .build();
            repository.save(s);
        }
    }

    @Test
    @DisplayName("기본적인 페이지 조회 테스트")
    void basicPageTest() {
        // given
        int pageNo = 5;
        int pageSize = 10;

        // 페이지 번호를 0부터 세기 때문에 -1을 해줘야 함 (1페이지 선택 -> 0으로 받아와야 함)
        Pageable pageRequest = PageRequest.of(
                pageNo - 1,
                pageSize,
//                Sort.by("name").descending() // 정렬 기준은 필드명으로 작성 (컬렁명X)
                Sort.by(Sort.Order.desc("name"),
                        Sort.Order.asc("city"))
        );


        // when
        Page<Student> allList = repository.findAll(pageRequest);

        // 실제 데이터 꺼내기
        List<Student> content = allList.getContent();

        // 총 페이지 수
        int totalPages = allList.getTotalPages();

        // 총 힉생 수
        long totalElements = allList.getTotalElements();

        // 다음 페이지, 이전 페이지 여부
        boolean next = allList.hasNext();
        boolean prev = allList.hasPrevious();

        // then
        System.out.println("총 페이지 수: " + totalPages);
        System.out.println("총 학생 수: " + totalElements);
        System.out.println("다음 페이지 여부: " + next);
        System.out.println("다음 페이지 여부: " + prev);
        content.forEach(System.out::println);
    }

    @Test
    @DisplayName("이름 검색 + 페이징")
    void searchAndPageTest() {
        // given
        int pageNo = 3;
        int pageSize = 10;
        Pageable pageRequest = PageRequest.of(pageNo - 1, pageSize);

        // when
        Page<Student> byNameContaining = repository.findByNameContaining("3", pageRequest);
        List<Student> content = byNameContaining.getContent();
        int totalPages = byNameContaining.getTotalPages();
        long totalElements = byNameContaining.getTotalElements();
        boolean next = byNameContaining.hasNext();
        boolean prev = byNameContaining.hasPrevious();

        /*
            페이징 처리 시에 버튼 알고리즘은 jpa에서 따로 제공하지 않기 때문에 버튼 배치 알고리즘을 수행할 클래스는 여전히 필요합니다.
            제공되는 정보는 이전보다 많기 때문에, 좀 더 수월하게 처리가 가능합니다.
        */

        // then
        System.out.println("총 페이지 수: " + totalPages);
        System.out.println("총 학생 수: " + totalElements);
        System.out.println("다음 페이지 여부: " + next);
        System.out.println("다음 페이지 여부: " + prev);
        content.forEach(System.out::println);

    }

}