package com.example.Qraft.repository;

import com.example.Qraft.entity.PostReaction;
import com.example.Qraft.entity.PostReaction.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    
    // 특정 사용자가 특정 게시글에 대한 반응 조회
    Optional<PostReaction> findByPostIdAndUserId(Long postId, Long userId);
    
    // 특정 게시글의 추천 수 조회
    @Query("SELECT COUNT(pr) FROM PostReaction pr WHERE pr.post.id = :postId AND pr.reactionType = :reactionType")
    long countByPostIdAndReactionType(@Param("postId") Long postId, @Param("reactionType") ReactionType reactionType);
    
    // 여러 게시글의 추천/비추천 수를 한 번에 조회 (성능 최적화)
    @Query("SELECT pr.post.id as postId, pr.reactionType as reactionType, COUNT(pr) as count " +
           "FROM PostReaction pr WHERE pr.post.id IN :postIds " +
           "GROUP BY pr.post.id, pr.reactionType")
    List<Object[]> findReactionCountsByPostIds(@Param("postIds") List<Long> postIds);
    
    // 특정 사용자의 여러 게시글에 대한 반응을 한 번에 조회
    @Query("SELECT pr FROM PostReaction pr WHERE pr.post.id IN :postIds AND pr.user.id = :userId")
    List<PostReaction> findByPostIdsAndUserId(@Param("postIds") List<Long> postIds, @Param("userId") Long userId);
    
    // 특정 게시글의 모든 반응 삭제 (게시글 삭제 시 사용)
    void deleteByPostId(Long postId);
}
