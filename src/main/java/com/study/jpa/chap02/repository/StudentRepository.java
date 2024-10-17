package com.study.jpa.chap02.repository;

import com.study.jpa.chap02.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {


    List<Student> findByName(String name);

    List<Student> findByCityAndMajor(String city, String major);

    List<Student> findByMajorContaining(String str);

    List<Student> findByMajorStartingWith(String str);

    List<Student> findByMajorEndingWith(String str);

//    List<Student> findByAgeLessThanEqual(int age);

//    List<Student> findByAgeGreaterThanEqual(int age);

//    List<Student> findByAgeBetween(int min, int max);

//    List<Student> findByAgeGreaterThan(int age);

//    List<Student> findByAgeLessThan(int age);

    /*
        > native-sql
        SELECT 컬럼명 FROM 테이블명
        WHERE 컬럼 = ?

        > JPQL
        SELECT 별칭 FROM 엔터티클래스명 AS 별칭
        WHERE 별칭.필드명 = ?
    */

    // native SQL 방식
    @Query(value = "select * from tbl_student where stu_name = :nm or city = :city", nativeQuery = true)
    List<Student> getStudentByNameOrCity(@Param("nm") String name, @Param("city") String city);

    // JPQL 방식
    @Query(value = "select st from Student st where st.name = ?1 or st.city = ?2")
    List<Student> getStudentByNameOrCity2(String name, String city);

    @Query("select st from Student st where st.name like %?1%")
    List<Student> searchByNameWithJPQL(String name);

    // 단일 조회
    @Query("select st from Student st where st.city = ?1")
    Optional<Student> getByCityWithJPQL(String city);

    @Modifying // select 아니면 무조건 붙여야 함
    @Query("delete from Student st where st.name = ?1 and st.city = ?2")
    void deleteByNameAndCityWithJPQL(String name, String city);
}

