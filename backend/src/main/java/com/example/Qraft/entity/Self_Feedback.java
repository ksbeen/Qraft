// src/entity/Self_Feedback.java
package com.example.Qraft.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "self_feedback")
public class Self_Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    // 이 필드 이름이 video_timestamp -> videoTimestamp 로 변경되었습니다.
    private Integer videoTimestamp;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "practice_log_id", nullable = false)
    private Practice_Log practiceLog;

    @Builder
    // 생성자의 파라미터 이름도 함께 변경되었습니다.
    public Self_Feedback(Integer videoTimestamp, String memo, Practice_Log practiceLog) {
        this.videoTimestamp = videoTimestamp;
        this.memo = memo;
        this.practiceLog = practiceLog;
    }
}