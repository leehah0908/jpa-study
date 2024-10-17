package com.study.jpa.chap03;

import com.study.jpa.chap02.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentPageRepository extends JpaRepository<Student, String> {

    // 이름에 특정 단어가 들어간 학생 조회 + 페이징 정보
    Page<Student> findByNameContaining(String name, Pageable pageable);
}
