package com.example.Qraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Qraft.entity.Practice_Log;

import java.util.List;

/**
 * @Repository: 이 인터페이스가 데이터베이스와 통신하는 Repository 계층의 컴포넌트(Bean)임을 나타냅니다.
 * JpaRepository<Practice_Log, Long>:
 * - Practice_Log: 이 Repository가 다룰 엔티티 클래스
 * - Long: 해당 엔티티의 ID 필드 타입
 */
@Repository
public interface PracticeLogRepository extends JpaRepository<Practice_Log, Long> {
    // JpaRepository를 상속받는 것만으로 기본적인 CRUD(save, findById, findAll, delete 등)
    // 메소드가 자동으로 생성되어 바로 사용할 수 있습니다.

    // userId를 기준으로 모든 Practice_Log를 찾아 시간 역순으로 정렬합니다.
    List<Practice_Log> findByUserIdOrderByCreatedAtDesc(Long userId);
}