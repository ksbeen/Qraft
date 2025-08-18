package com.example.Qraft.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Entity: 이 클래스가 데이터베이스의 테이블과 1:1로 매핑되는 JPA 엔티티임을 나타냅니다.
 * @Table(name = "practice_log"): 실제 DB의 'practice_log' 테이블과 매핑됨을 명시합니다.
 * @Getter: Lombok 어노테이션으로, 모든 필드에 대한 getter 메소드를 자동으로 생성합니다.
 * @NoArgsConstructor(access = AccessLevel.PROTECTED): JPA는 엔티티를 생성할 때 기본 생성자를 필요로 합니다.
 * 이 어노테이션은 외부에서 무분별하게 객체를 생성하는 것을 막기 위해 protected 레벨의 기본 생성자를 만들어줍니다.
 * @EntityListeners(AuditingEntityListener.class): JPA Auditing 기능을 활성화하여, createdAt 같은 필드가 자동으로 채워지도록 합니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "practice_log")
public class Practice_Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 녹화된 영상 파일의 서버 내 저장 경로 또는 파일명을 저장하는 컬럼입니다.
    @Column(nullable = false)
    private String video_url;

    // @CreatedDate: 엔티티가 처음 저장될 때의 시간이 자동으로 기록됩니다.
    @CreatedDate
    @Column(updatable = false) // 한번 생성되면 수정되지 않도록 설정합니다.
    private LocalDateTime createdAt; // 자바 표준 네이밍 컨벤션(camelCase)을 따릅니다.

    // --- 관계 설정 ---

    // @ManyToOne: 다대일 관계. 여러 개의 면접 기록(N)이 하나의 사용자(1)에게 속합니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // DB의 'user_id' 컬럼과 매핑됩니다.
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_set_id", nullable = false) // DB의 'interview_set_id' 컬럼과 매핑됩니다.
    private InterviewSet interviewSet;

    /**
     * @Builder: 빌더 패턴을 사용하여 객체를 생성할 수 있게 해주는 Lombok 어노테이션입니다.
     * 어떤 필드에 어떤 값을 넣을지 명확하게 지정할 수 있어 안전하고 가독성이 좋습니다.
     */
    @Builder
    public Practice_Log(String video_url, User user, InterviewSet interviewSet) {
        this.video_url = video_url;
        this.user = user;
        this.interviewSet = interviewSet;
    }
}