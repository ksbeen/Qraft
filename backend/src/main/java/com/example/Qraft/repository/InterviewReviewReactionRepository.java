package com.example.Qraft.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Qraft.entity.InterviewReviewReaction;
import com.example.Qraft.entity.PostReaction.ReactionType;

@Repository
public interface InterviewReviewReactionRepository extends JpaRepository<InterviewReviewReaction, Long> {
    
    /**
     * 특정 사용자가 특정 면접 후기에 남긴 반응 조회
     */
    Optional<InterviewReviewReaction> findByInterviewReviewIdAndUserId(Long interviewReviewId, Long userId);
    
    /**
     * 특정 면접 후기의 추천 수 조회
     */
    Long countByInterviewReviewIdAndReactionType(Long interviewReviewId, ReactionType reactionType);
    
    /**
     * 여러 면접 후기의 반응 수를 배치로 조회 (성능 최적화)
     */
    @Query("SELECT ir.id as interviewReviewId, irr.reactionType, COUNT(irr) as count " +
           "FROM InterviewReview ir LEFT JOIN InterviewReviewReaction irr ON ir.id = irr.interviewReview.id " +
           "WHERE ir.id IN :interviewReviewIds " +
           "GROUP BY ir.id, irr.reactionType")
    List<Map<String, Object>> findReactionCountsByInterviewReviewIds(@Param("interviewReviewIds") List<Long> interviewReviewIds);
    
    /**
     * 특정 사용자가 여러 면접 후기에 남긴 반응을 배치로 조회
     */
    @Query("SELECT irr FROM InterviewReviewReaction irr " +
           "WHERE irr.interviewReview.id IN :interviewReviewIds AND irr.user.id = :userId")
    List<InterviewReviewReaction> findByInterviewReviewIdsAndUserId(@Param("interviewReviewIds") List<Long> interviewReviewIds, @Param("userId") Long userId);
    
    /**
     * 특정 면접 후기의 모든 반응 삭제 (cascade deletion용)
     */
    void deleteByInterviewReviewId(Long interviewReviewId);
}