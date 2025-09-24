package com.example.Qraft.service;

import com.example.Qraft.dto.CommentRequestDto;
import com.example.Qraft.dto.CommentResponseDto;
import com.example.Qraft.entity.Comment;
import com.example.Qraft.entity.Post;
import com.example.Qraft.entity.User;
import com.example.Qraft.repository.CommentRepository;
import com.example.Qraft.repository.PostRepository;
import com.example.Qraft.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 댓글 서비스
 */
@Service
@Transactional
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    public CommentService(CommentRepository commentRepository, 
                         PostRepository postRepository,
                         UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * 특정 게시글의 모든 댓글 조회
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        // 게시글 존재 확인
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId);
        }
        
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
        return comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * 댓글 작성
     */
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto) {
        // 현재 로그인한 사용자 정보 가져오기
        User currentUser = getCurrentUser();
        
        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
        
        // 댓글 생성
        Comment comment = new Comment(requestDto.getContent(), post, currentUser);
        Comment savedComment = commentRepository.save(comment);
        
        return new CommentResponseDto(savedComment);
    }
    
    /**
     * 댓글 수정
     */
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. ID: " + commentId));
        
        // 작성자 확인
        User currentUser = getCurrentUser();
        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("댓글 수정 권한이 없습니다.");
        }
        
        // 댓글 내용 수정
        comment.updateContent(requestDto.getContent());
        Comment updatedComment = commentRepository.save(comment);
        
        return new CommentResponseDto(updatedComment);
    }
    
    /**
     * 댓글 삭제
     */
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. ID: " + commentId));
        
        // 작성자 확인
        User currentUser = getCurrentUser();
        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("댓글 삭제 권한이 없습니다.");
        }
        
        commentRepository.delete(comment);
    }
    
    /**
     * 특정 게시글의 댓글 개수 조회
     */
    @Transactional(readOnly = true)
    public long getCommentCount(Long postId) {
        return commentRepository.countByPostId(postId);
    }
    
    /**
     * 현재 로그인한 사용자 정보 가져오기
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
}
