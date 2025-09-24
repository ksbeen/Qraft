package com.example.Qraft.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "interview_review")
public class InterviewReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String company; // 회사명

    @Column(nullable = false)
    private String position; // 지원 포지션

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer views = 0;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;
    
    // 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public InterviewReview(String title, String content, String company, String position, User user) {
        this.title = title;
        this.content = content;
        this.company = company;
        this.position = position;
        this.user = user;
    }

    /**
     * 면접 후기의 내용을 수정하는 메소드
     */
    public void update(String title, String content, String company, String position) {
        this.title = title;
        this.content = content;
        this.company = company;
        this.position = position;
    }

    /**
     * 조회수를 1 증가시키는 메소드
     */
    public void increaseViews() {
        this.views++;
    }
}