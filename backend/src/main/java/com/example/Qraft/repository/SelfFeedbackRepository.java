package com.example.Qraft.repository;

import com.example.Qraft.entity.Self_Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SelfFeedbackRepository extends JpaRepository<Self_Feedback, Long> {
    // 특정 면접 기록(practiceLogId)에 속한 모든 피드백을 시간 순서대로 조회합니다.
    List<Self_Feedback> findByPracticeLogIdOrderByVideoTimestampAsc(Long practiceLogId);
}