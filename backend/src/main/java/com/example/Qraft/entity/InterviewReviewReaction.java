package com.example.Qraft.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.Qraft.entity.PostReaction.ReactionType;

/**
 * 면접 후기 추천/비추천 엔티티
 */
@Entity
@Table(name = "interview_review_reactions")
public class InterviewReviewReaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType reactionType;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    // 면접 후기와의 관계 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_review_id", nullable = false)
    private InterviewReview interviewReview;
    
    // 사용자와의 관계 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // 기본 생성자
    protected InterviewReviewReaction() {}
    
    // 생성자
    public InterviewReviewReaction(ReactionType reactionType, InterviewReview interviewReview, User user) {
        this.reactionType = reactionType;
        this.interviewReview = interviewReview;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }
    
    // 반응 타입 변경
    public void updateReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }
    
    // Getter 메서드들
    public Long getId() {
        return id;
    }
    
    public ReactionType getReactionType() {
        return reactionType;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public InterviewReview getInterviewReview() {
        return interviewReview;
    }
    
    public User getUser() {
        return user;
    }
}