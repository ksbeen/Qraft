package com.example.Qraft.service;

import com.example.Qraft.dto.InterviewReviewCommentRequestDto;
import com.example.Qraft.dto.InterviewReviewCommentResponseDto;
import com.example.Qraft.entity.InterviewReview;
import com.example.Qraft.entity.InterviewReviewComment;
import com.example.Qraft.entity.User;
import com.example.Qraft.repository.InterviewReviewCommentRepository;
import com.example.Qraft.repository.InterviewReviewRepository;
import com.example.Qraft.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 면접 후기 댓글 서비스
 */
@Service
@Transactional
public class InterviewReviewCommentService {
    
    private final InterviewReviewCommentRepository commentRepository;
    private final InterviewReviewRepository reviewRepository;
    private final UserRepository userRepository;
    
    public InterviewReviewCommentService(InterviewReviewCommentRepository commentRepository, 
                                       InterviewReviewRepository reviewRepository,
                                       UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * 특정 면접 후기의 모든 댓글 조회
     */
    @Transactional(readOnly = true)
    public List<InterviewReviewCommentResponseDto> getCommentsByReviewId(Long reviewId) {
        // 면접 후기 존재 확인
        if (!reviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("면접 후기를 찾을 수 없습니다. ID: " + reviewId);
        }
        
        List<InterviewReviewComment> comments = commentRepository.findByInterviewReviewIdOrderByCreatedAtAsc(reviewId);
        return comments.stream()
                .map(InterviewReviewCommentResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * 댓글 작성
     */
    public InterviewReviewCommentResponseDto createComment(Long reviewId, InterviewReviewCommentRequestDto requestDto) {
        // 현재 로그인한 사용자 정보 가져오기
        User currentUser = getCurrentUser();
        
        // 면접 후기 조회
        InterviewReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("면접 후기를 찾을 수 없습니다. ID: " + reviewId));
        
        // 댓글 생성
        InterviewReviewComment comment = new InterviewReviewComment(requestDto.getContent(), review, currentUser);
        InterviewReviewComment savedComment = commentRepository.save(comment);
        
        return new InterviewReviewCommentResponseDto(savedComment);
    }
    
    /**
     * 댓글 수정
     */
    public InterviewReviewCommentResponseDto updateComment(Long commentId, InterviewReviewCommentRequestDto requestDto) {
        InterviewReviewComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. ID: " + commentId));
        
        // 작성자 확인
        User currentUser = getCurrentUser();
        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }
        
        // 댓글 내용 수정
        comment.updateContent(requestDto.getContent());
        InterviewReviewComment updatedComment = commentRepository.save(comment);
        
        return new InterviewReviewCommentResponseDto(updatedComment);
    }
    
    /**
     * 댓글 삭제
     */
    public void deleteComment(Long commentId) {
        InterviewReviewComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. ID: " + commentId));
        
        // 작성자 확인
        User currentUser = getCurrentUser();
        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }
        
        commentRepository.delete(comment);
    }
    
    /**
     * 특정 면접 후기의 댓글 개수 조회
     */
    @Transactional(readOnly = true)
    public long getCommentCount(Long reviewId) {
        return commentRepository.countByInterviewReviewId(reviewId);
    }
    
    /**
     * 현재 로그인한 사용자 정보 가져오기
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }
        
        Object principal = authentication.getPrincipal();
        String username;
        
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));
    }
}