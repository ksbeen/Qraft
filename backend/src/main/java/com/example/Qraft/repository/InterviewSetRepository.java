package com.example.Qraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Qraft.entity.InterviewSet;

@Repository
public interface InterviewSetRepository extends JpaRepository<InterviewSet, Long> {
    // JpaRepository를 상속받는 것만으로 기본적인 CRUD 기능이 자동 생성됩니다.
}