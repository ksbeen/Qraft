package com.example.Qraft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Qraft.entity.InterviewReview;

@Repository
public interface InterviewReviewRepository extends JpaRepository<InterviewReview, Long> {
    
    /**
     * 면접 후기를 생성일 기준 내림차순으로 정렬하여 조회
     */
    @Query("SELECT ir FROM InterviewReview ir ORDER BY ir.created_at DESC")
    List<InterviewReview> findAllOrderByCreatedAtDesc();
    
    /**
     * 특정 사용자가 작성한 면접 후기 조회
     */
    @Query("SELECT ir FROM InterviewReview ir WHERE ir.user.id = :userId ORDER BY ir.created_at DESC")
    List<InterviewReview> findByUserIdOrderByCreated_atDesc(@Param("userId") Long userId);
    
    /**
     * 회사명으로 면접 후기 검색
     */
    @Query("SELECT ir FROM InterviewReview ir WHERE LOWER(ir.company) LIKE LOWER(CONCAT('%', :company, '%')) ORDER BY ir.created_at DESC")
    List<InterviewReview> findByCompanyContainingIgnoreCaseOrderByCreated_atDesc(@Param("company") String company);
    
    /**
     * 제목으로 면접 후기 검색
     */
    @Query("SELECT ir FROM InterviewReview ir WHERE LOWER(ir.title) LIKE LOWER(CONCAT('%', :title, '%')) ORDER BY ir.created_at DESC")
    List<InterviewReview> findByTitleContainingIgnoreCaseOrderByCreated_atDesc(@Param("title") String title);
}