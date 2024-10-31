package com.study.jpa.chap04.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"department"})
@EqualsAndHashCode(of = "id")
@Builder

@Entity
@Table(name = "tbl_emp")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private long id;

    @Column(name = "emp_name", nullable = false)
    private String name;

    // EAGER: 해당 필드를 사용 하든 말든 항상 무조건 조인을 수행
    // LAZY: 필요한 경우에만 데이터를 가져온다. -> 실무
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id") // FK 컬럼명 (연관 테이블의 컬럼명과 일치하게)
    private Department department;

    // 연관관계 편의 메서드 (양방향에서 연관 필드가 수정될 경우 실제 테이블과의 데이터를 맞춰주기 위한 메서드 선언)
    public void changeDepartment(Department department) {
        this.department = department;
        department.getEmployees().add(this);
    }
}
