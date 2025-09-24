package com.example.Qraft.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 면접 후기 댓글 엔티티
 */
@Entity
@Table(name = "interview_review_comments")
public class InterviewReviewComment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // 면접 후기와의 관계 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_review_id", nullable = false)
    private InterviewReview interviewReview;
    
    // 작성자와의 관계 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    // 기본 생성자
    protected InterviewReviewComment() {}
    
    // 생성자
    public InterviewReviewComment(String content, InterviewReview interviewReview, User author) {
        this.content = content;
        this.interviewReview = interviewReview;
        this.author = author;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // 댓글 수정
    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getter 메서드들
    public Long getId() {
        return id;
    }
    
    public String getContent() {
        return content;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public InterviewReview getInterviewReview() {
        return interviewReview;
    }
    
    public User getAuthor() {
        return author;
    }
}