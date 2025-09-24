package com.example.Qraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Qraft.entity.Post;
import java.util.List;

// @Repository: 이 인터페이스가 데이터베이스와 통신하는 Repository임을 나타냅니다.
@Repository
// JpaRepository<Post, Long>의 의미:
// - Post: 이 Repository가 다룰 Entity.
// - Long: Post Entity의 기본 키(PK)의 타입.
public interface PostRepository extends JpaRepository<Post, Long> {
    // 게시글을 생성일 기준으로 내림차순 정렬하여 조회 (최신 게시글이 먼저)
    @Query("SELECT p FROM Post p ORDER BY p.created_at DESC")
    List<Post> findAllOrderByCreatedAtDesc();
}