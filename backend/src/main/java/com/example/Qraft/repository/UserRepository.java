package com.example.Qraft.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Qraft.entity.User;

// @Repository: 이 인터페이스가 Spring Data JPA의 Repository임을 Spring 프레임워크에 알려줍니다.
@Repository
// UserRepository 인터페이스는 JpaRepository를 상속(extends)받습니다.
// JpaRepository<User, Long>의 의미:
// - User: 이 Repository가 어떤 Entity를 다룰 것인지를 나타냅니다. (우리가 만든 User 클래스)
// - Long: 해당 Entity의 Primary Key(기본 키)의 데이터 타입을 나타냅니다. (User 클래스의 id 필드 타입)
public interface UserRepository extends JpaRepository<User, Long> {

    // JpaRepository를 상속받는 것만으로도 아래와 같은 기본적인 DB操作 메소드들이 자동으로 생성됩니다.
    // - save(User): 유저 저장 (INSERT, UPDATE)
    // - findById(Long id): id로 유저 찾기 (SELECT)
    // - findAll(): 모든 유저 찾기 (SELECT)
    // - deleteById(Long id): id로 유저 삭제 (DELETE)

    
    // findByEmail: 이메일을 기준으로 사용자를 찾는 메소드입니다.
    // Spring Data JPA가 메소드 이름을 분석해서 자동으로 SQL 쿼리를 만들어줍니다.
    // Optional<User>는 사용자가 존재할 수도, 존재하지 않을 수도 있다는 것을 나타냅니다.
    Optional<User> findByEmail(String email);
}