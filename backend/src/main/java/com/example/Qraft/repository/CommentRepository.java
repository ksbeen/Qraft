package com.example.Qraft.repository;

import com.example.Qraft.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 댓글 Repository
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    /**
     * 특정 게시글의 모든 댓글을 생성시간 순으로 조회
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.post.id = :postId ORDER BY c.createdAt ASC")
    List<Comment> findByPostIdOrderByCreatedAtAsc(@Param("postId") Long postId);
    
    /**
     * 특정 게시글의 댓글 개수 조회
     */
    long countByPostId(Long postId);
    
    /**
     * 특정 게시글의 모든 댓글 삭제
     */
    void deleteByPostId(Long postId);
    
    /**
     * 특정 사용자가 작성한 댓글 조회
     */
    List<Comment> findByAuthorIdOrderByCreatedAtDesc(Long authorId);
}
