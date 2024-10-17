package com.study.jpa.chap02.repository;

import com.study.jpa.chap02.entity.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Test
    void insertData() {
        Student s1 = Student.builder()
                .name("홍길동")
                .city("경기도")
                .major("체육학")
                .build();
        Student s2 = Student.builder()
                .name("김철수")
                .city("서울특별시")
                .major("컴퓨터공학")
                .build();
        Student s3 = Student.builder()
                .name("박영희")
                .city("제주도")
                .major("산업공학")
                .build();
        studentRepository.save(s1);
        studentRepository.save(s2);
        studentRepository.save(s3);
    }

    @Test
    @DisplayName("박영희 학생의 모든 정보 조회")
    void findByNameTest() {
        // given
        String name = "박영희";

        // when
        List<Student> byName = studentRepository.findByName(name);

        // then
        Assertions.assertEquals(1,byName.size());
        System.out.println(byName.get(0));
    }

    @Test
    @DisplayName("도시 이름과 전공으로 검색")
    void findByCityMajorTest() {
        // given
        String city = "제주도";
        String major = "산업공학";

        // when
        List<Student> byCityAndMajor = studentRepository.findByCityAndMajor(city, major);

        // then
        System.out.println(byCityAndMajor);
    }

    @Test
    @DisplayName("'공'이 들어가는 전공을 가진 학생 조회")
    void findByMajorContainingTest() {
        // given
        String str = "공";

        // when
        List<Student> byMajorContaining = studentRepository.findByMajorContaining(str);

        // then
        System.out.println(byMajorContaining);
    }

    @Test
    @DisplayName("'산'으로 시작하는 전공을 가진 학생 조회")
    void findByMajorStartingWithTest() {
        // given
        String str = "산";

        // when
        List<Student> byMajorStartingWith = studentRepository.findByMajorStartingWith(str);

        // then
        System.out.println(byMajorStartingWith);
    }

    @Test
    @DisplayName("'공학'으로 끝나는 전공을 가진 학생 조회")
    void findByMajorEndingWithTest() {
        // given
        String str = "공학";

        // when
        List<Student> byMajorEndingWith = studentRepository.findByMajorEndingWith(str);

        // then
        System.out.println(byMajorEndingWith);
    }

    @Test
    @DisplayName("도시 또는 이름으로 학생 조회")
    void nativeSQLTest() {
        // given
        String name = "홍길동";
        String city = "제주도";

        // when
        List<Student> studentByNameOrCity = studentRepository.getStudentByNameOrCity(name, city);

        // then
        System.out.println(studentByNameOrCity);
    }

    @Test
    @DisplayName("JPQL로 like 쿼리 테스트")
    void JPQLLikeTest() {
        // given
        String str = "영";

        // when
        List<Student> students = studentRepository.searchByNameWithJPQL(str);

        // then
        System.out.println(students);
    }

    @Test
    @DisplayName("JPQL로 삭제하기")
    void deleteTest () {
        // given
        String name = "홍길동";
        String city = "경기도";

        // when
        studentRepository.deleteByNameAndCityWithJPQL(name, city);

        // then
        Assertions.assertEquals(0, studentRepository.findByName(name).size());
    }
}