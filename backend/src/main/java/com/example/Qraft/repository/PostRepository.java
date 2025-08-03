package com.example.Qraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Qraft.entity.Post;

// @Repository: 이 인터페이스가 데이터베이스와 통신하는 Repository임을 나타냅니다.
@Repository
// JpaRepository<Post, Long>의 의미:
// - Post: 이 Repository가 다룰 Entity.
// - Long: Post Entity의 기본 키(PK)의 타입.
public interface PostRepository extends JpaRepository<Post, Long> {
    // 이 안은 비워둡니다.
    // JpaRepository를 상속받는 것만으로 기본적인 CRUD 메소드가 자동으로 생성됩니다.
}