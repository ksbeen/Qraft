package com.example.Qraft.controller;

import com.example.Qraft.dto.InterviewReviewCommentRequestDto;
import com.example.Qraft.dto.InterviewReviewCommentResponseDto;
import com.example.Qraft.service.InterviewReviewCommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 면접 후기 댓글 컨트롤러
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class InterviewReviewCommentController {
    
    private final InterviewReviewCommentService commentService;
    
    public InterviewReviewCommentController(InterviewReviewCommentService commentService) {
        this.commentService = commentService;
    }
    
    /**
     * 특정 면접 후기의 모든 댓글 조회
     * GET /api/interview-reviews/{reviewId}/comments
     */
    @GetMapping("/interview-reviews/{reviewId}/comments")
    public ResponseEntity<List<InterviewReviewCommentResponseDto>> getComments(@PathVariable Long reviewId) {
        List<InterviewReviewCommentResponseDto> comments = commentService.getCommentsByReviewId(reviewId);
        return ResponseEntity.ok(comments);
    }
    
    /**
     * 댓글 작성
     * POST /api/interview-reviews/{reviewId}/comments
     */
    @PostMapping("/interview-reviews/{reviewId}/comments")
    public ResponseEntity<InterviewReviewCommentResponseDto> createComment(
            @PathVariable Long reviewId,
            @Valid @RequestBody InterviewReviewCommentRequestDto requestDto) {
        InterviewReviewCommentResponseDto comment = commentService.createComment(reviewId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }
    
    /**
     * 댓글 수정
     * PUT /api/interview-reviews/comments/{commentId}
     */
    @PutMapping("/interview-reviews/comments/{commentId}")
    public ResponseEntity<InterviewReviewCommentResponseDto> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody InterviewReviewCommentRequestDto requestDto) {
        InterviewReviewCommentResponseDto comment = commentService.updateComment(commentId, requestDto);
        return ResponseEntity.ok(comment);
    }
    
    /**
     * 댓글 삭제
     * DELETE /api/interview-reviews/comments/{commentId}
     */
    @DeleteMapping("/interview-reviews/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 특정 면접 후기의 댓글 개수 조회
     * GET /api/interview-reviews/{reviewId}/comments/count
     */
    @GetMapping("/interview-reviews/{reviewId}/comments/count")
    public ResponseEntity<Long> getCommentCount(@PathVariable Long reviewId) {
        long count = commentService.getCommentCount(reviewId);
        return ResponseEntity.ok(count);
    }
}