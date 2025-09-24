package com.example.Qraft.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Qraft.dto.InterviewReviewCreateRequestDto;
import com.example.Qraft.dto.InterviewReviewResponseDto;
import com.example.Qraft.dto.InterviewReviewUpdateRequestDto;
import com.example.Qraft.entity.InterviewReview;
import com.example.Qraft.entity.InterviewReviewReaction;
import com.example.Qraft.entity.PostReaction.ReactionType;
import com.example.Qraft.entity.User;
import com.example.Qraft.repository.InterviewReviewCommentRepository;
import com.example.Qraft.repository.InterviewReviewReactionRepository;
import com.example.Qraft.repository.InterviewReviewRepository;
import com.example.Qraft.repository.UserRepository;

@Service
public class InterviewReviewService {

    @Autowired
    private InterviewReviewRepository interviewReviewRepository;
    
    @Autowired
    private InterviewReviewCommentRepository commentRepository;
    
    @Autowired
    private InterviewReviewReactionRepository reactionRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * 모든 면접 후기 조회 (최신순, 성능 최적화된 버전)
     */
    @Transactional(readOnly = true)
    public List<InterviewReviewResponseDto> findAll() {
        List<InterviewReview> reviews = interviewReviewRepository.findAllOrderByCreatedAtDesc();
        
        if (reviews.isEmpty()) {
            return List.of();
        }
        
        return reviews.stream()
                .map(review -> {
                    // 반응 수 조회
                    Long recommendCount = reactionRepository.countByInterviewReviewIdAndReactionType(
                            review.getId(), ReactionType.RECOMMEND);
                    Long opposeCount = reactionRepository.countByInterviewReviewIdAndReactionType(
                            review.getId(), ReactionType.OPPOSE);
                    
                    // 댓글 수 조회
                    Long commentCount = commentRepository.countByInterviewReviewId(review.getId());
                    
                    return new InterviewReviewResponseDto(review, 
                            recommendCount.intValue(), 
                            opposeCount.intValue(), 
                            commentCount.intValue(), 
                            null);
                })
                .collect(Collectors.toList());
    }

    /**
     * 특정 면접 후기 조회
     */
    @Transactional(readOnly = true)
    public InterviewReviewResponseDto findById(Long id) {
        InterviewReview review = interviewReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("면접 후기를 찾을 수 없습니다. ID: " + id));
        
        // 반응 수 조회
        Long recommendCount = reactionRepository.countByInterviewReviewIdAndReactionType(id, ReactionType.RECOMMEND);
        Long opposeCount = reactionRepository.countByInterviewReviewIdAndReactionType(id, ReactionType.OPPOSE);
        
        // 댓글 수 조회
        Long commentCount = commentRepository.countByInterviewReviewId(id);
        
        return new InterviewReviewResponseDto(review, 
                recommendCount.intValue(), 
                opposeCount.intValue(), 
                commentCount.intValue(), 
                null);
    }

    /**
     * 면접 후기 생성
     */
    @Transactional
    public InterviewReview create(String userEmail, InterviewReviewCreateRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. email=" + userEmail));
        
        InterviewReview review = InterviewReview.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .company(requestDto.getCompany())
                .position(requestDto.getPosition())
                .user(user)
                .build();
        
        return interviewReviewRepository.save(review);
    }

    /**
     * 면접 후기 수정
     */
    @Transactional
    public InterviewReview update(Long id, String userEmail, InterviewReviewUpdateRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. email=" + userEmail));
        
        InterviewReview review = interviewReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("면접 후기를 찾을 수 없습니다. ID: " + id));
        
        // 작성자 확인
        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("면접 후기를 수정할 권한이 없습니다.");
        }
        
        review.update(requestDto.getTitle(), requestDto.getContent(), 
                     requestDto.getCompany(), requestDto.getPosition());
        return interviewReviewRepository.save(review);
    }

    /**
     * 면접 후기 삭제 (cascade deletion)
     */
    @Transactional
    public void delete(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. email=" + userEmail));
        
        InterviewReview review = interviewReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("면접 후기를 찾을 수 없습니다. ID: " + id));
        
        // 작성자 확인
        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("면접 후기를 삭제할 권한이 없습니다.");
        }
        
        // Cascade deletion: 반응 → 댓글 → 면접 후기 순서로 삭제
        reactionRepository.deleteByInterviewReviewId(id);
        commentRepository.deleteByInterviewReviewId(id);
        interviewReviewRepository.delete(review);
    }

    /**
     * 조회수 증가
     */
    @Transactional
    public void increaseViews(Long id) {
        InterviewReview review = interviewReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("면접 후기를 찾을 수 없습니다. ID: " + id));
        
        review.increaseViews();
        interviewReviewRepository.save(review);
    }

    /**
     * 추천/비추천 토글
     */
    @Transactional
    public void toggleReaction(Long reviewId, String userEmail, ReactionType reactionType) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. email=" + userEmail));
        
        InterviewReview review = interviewReviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("면접 후기를 찾을 수 없습니다. ID: " + reviewId));
        
        Optional<InterviewReviewReaction> existingReaction = 
                reactionRepository.findByInterviewReviewIdAndUserId(reviewId, user.getId());
        
        if (existingReaction.isPresent()) {
            InterviewReviewReaction reaction = existingReaction.get();
            if (reaction.getReactionType() == reactionType) {
                // 같은 반응이면 삭제
                reactionRepository.delete(reaction);
            } else {
                // 다른 반응이면 변경
                reaction.updateReactionType(reactionType);
                reactionRepository.save(reaction);
            }
        } else {
            // 새로운 반응 생성
            InterviewReviewReaction newReaction = new InterviewReviewReaction(reactionType, review, user);
            reactionRepository.save(newReaction);
        }
    }
}