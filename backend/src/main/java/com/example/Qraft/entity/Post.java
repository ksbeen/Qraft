package com.example.Qraft.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // Auditing 기능 사용
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT") // DB의 TEXT 타입과 매핑
    private String content;

    // ENUM 타입을 데이터베이스에 저장할 때, 기본값인 순서(숫자)가 아닌 문자열 자체로 저장하도록 설정합니다.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardType board_type;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate // Entity가 수정될 때 시간이 자동으로 기록됩니다.
    private LocalDateTime updated_at;
    
    // --- 관계 설정 ---

    // @ManyToOne: 다대일 관계를 나타냅니다. 게시글(N)은 사용자(1)에게 속합니다.
    // FetchType.LAZY는 Post를 조회할 때 바로 User 정보를 가져오지 않고, 실제로 User 정보가 필요할 때 가져오도록 하는 성능 최적화 옵션입니다.
    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn: 외래 키(FK)를 매핑할 때 사용합니다. 'user_id' 컬럼을 통해 User 테이블과 조인합니다.
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // 빌더 패턴을 사용하여 객체를 생성합니다.
    @Builder
    public Post(String title, String content, BoardType board_type, User user) {
        this.title = title;
        this.content = content;
        this.board_type = board_type;
        this.user = user;
    }

    /**
     * 게시글의 제목과 내용을 수정하는 메소드
     * @param title 새로운 제목
     * @param content 새로운 내용
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}