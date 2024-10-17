package com.study.jpa.chap02.repository;

import com.study.jpa.chap02.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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
}

