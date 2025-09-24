package com.example.Qraft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Qraft.entity.InterviewReviewComment;

@Repository
public interface InterviewReviewCommentRepository extends JpaRepository<InterviewReviewComment, Long> {
    
    /**
     * 특정 면접 후기의 댓글을 생성일 기준 오름차순으로 조회
     */
    List<InterviewReviewComment> findByInterviewReviewIdOrderByCreatedAtAsc(Long interviewReviewId);
    
    /**
     * 특정 면접 후기의 댓글을 생성일 기준 오름차순으로 조회 (작성자 정보 포함)
     */
    @Query("SELECT c FROM InterviewReviewComment c JOIN FETCH c.author WHERE c.interviewReview.id = :interviewReviewId ORDER BY c.createdAt ASC")
    List<InterviewReviewComment> findByInterviewReviewIdWithAuthor(@Param("interviewReviewId") Long interviewReviewId);
    
    /**
     * 특정 면접 후기의 댓글 수 조회
     */
    Long countByInterviewReviewId(Long interviewReviewId);
    
    /**
     * 특정 면접 후기의 모든 댓글 삭제 (cascade deletion용)
     */
    void deleteByInterviewReviewId(Long interviewReviewId);
}