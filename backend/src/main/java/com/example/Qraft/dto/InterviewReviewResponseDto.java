package com.example.Qraft.dto;

import java.time.LocalDateTime;

import com.example.Qraft.entity.InterviewReview;
import com.example.Qraft.entity.PostReaction.ReactionType;

import lombok.Getter;

@Getter
public class InterviewReviewResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String company;
    private final String position;
    private final Integer views;
    private final Long authorId;
    private final String authorNickname;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Integer recommendCount;
    private final Integer opposeCount;
    private final Integer commentCount;
    private final ReactionType currentUserReaction;

    /**
     * InterviewReview Entity 객체를 인자로 받아서 InterviewReviewResponseDto를 생성하는 생성자
     */
    public InterviewReviewResponseDto(InterviewReview review) {
        this.id = review.getId();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.company = review.getCompany();
        this.position = review.getPosition();
        this.views = review.getViews();
        this.authorId = review.getUser().getId();
        this.authorNickname = review.getUser().getNickname();
        this.createdAt = review.getCreated_at();
        this.updatedAt = review.getUpdated_at();
        this.recommendCount = 0;
        this.opposeCount = 0;
        this.commentCount = 0;
        this.currentUserReaction = null;
    }

    /**
     * 추천/비추천, 댓글 정보를 포함한 생성자
     */
    public InterviewReviewResponseDto(InterviewReview review, Integer recommendCount, Integer opposeCount, 
                                    Integer commentCount, ReactionType currentUserReaction) {
        this.id = review.getId();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.company = review.getCompany();
        this.position = review.getPosition();
        this.views = review.getViews();
        this.authorId = review.getUser().getId();
        this.authorNickname = review.getUser().getNickname();
        this.createdAt = review.getCreated_at();
        this.updatedAt = review.getUpdated_at();
        this.recommendCount = recommendCount;
        this.opposeCount = opposeCount;
        this.commentCount = commentCount;
        this.currentUserReaction = currentUserReaction;
    }
}