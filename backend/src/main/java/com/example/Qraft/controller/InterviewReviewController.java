package com.example.Qraft.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Qraft.dto.InterviewReviewCreateRequestDto;
import com.example.Qraft.dto.InterviewReviewResponseDto;
import com.example.Qraft.dto.InterviewReviewUpdateRequestDto;
import com.example.Qraft.entity.InterviewReview;
import com.example.Qraft.entity.PostReaction.ReactionType;
import com.example.Qraft.service.InterviewReviewService;

@RestController
@RequestMapping("/api/interview-reviews")
public class InterviewReviewController {

    @Autowired
    private InterviewReviewService interviewReviewService;

    /**
     * 모든 면접 후기 조회
     */
    @GetMapping
    public ResponseEntity<List<InterviewReviewResponseDto>> getAllReviews() {
        List<InterviewReviewResponseDto> reviews = interviewReviewService.findAll();
        return ResponseEntity.ok(reviews);
    }

    /**
     * 특정 면접 후기 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<InterviewReviewResponseDto> getReview(@PathVariable Long id) {
        InterviewReviewResponseDto review = interviewReviewService.findById(id);
        return ResponseEntity.ok(review);
    }

    /**
     * 면접 후기 생성
     */
    @PostMapping
    public ResponseEntity<InterviewReview> createReview(
            @RequestBody InterviewReviewCreateRequestDto requestDto,
            Principal principal) {
        
        String userEmail = principal.getName();
        InterviewReview createdReview = interviewReviewService.create(userEmail, requestDto);
        return ResponseEntity.ok(createdReview);
    }

    /**
     * 면접 후기 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<InterviewReview> updateReview(
            @PathVariable Long id,
            @RequestBody InterviewReviewUpdateRequestDto requestDto,
            Principal principal) {
        
        String userEmail = principal.getName();
        InterviewReview updatedReview = interviewReviewService.update(id, userEmail, requestDto);
        return ResponseEntity.ok(updatedReview);
    }

    /**
     * 면접 후기 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, Principal principal) {
        String userEmail = principal.getName();
        interviewReviewService.delete(id, userEmail);
        return ResponseEntity.ok().build();
    }

    /**
     * 조회수 증가
     */
    @PostMapping("/{id}/views")
    public ResponseEntity<Void> increaseViews(@PathVariable Long id) {
        interviewReviewService.increaseViews(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 추천/비추천 토글
     */
    @PostMapping("/{id}/reactions")
    public ResponseEntity<Void> toggleReaction(
            @PathVariable Long id,
            @RequestParam ReactionType reactionType,
            Principal principal) {
        
        String userEmail = principal.getName();
        interviewReviewService.toggleReaction(id, userEmail, reactionType);
        return ResponseEntity.ok().build();
    }
}