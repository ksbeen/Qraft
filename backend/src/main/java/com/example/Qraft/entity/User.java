package com.example.Qraft.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Builder;


// Lombok 어노테이션: 클래스의 모든 필드에 대한 getter 메소드를 자동으로 생성해줍니다.
@Getter
// Lombok 어노테이션: 파라미터가 없는 기본 생성자를 자동으로 생성해줍니다.
// access = AccessLevel.PROTECTED는 외부에서 무분별한 객체 생성을 막아주는 역할을 합니다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// JPA 어노테이션: 이 클래스가 데이터베이스의 테이블과 매핑되는 'Entity' 클래스임을 알려줍니다.
@Entity
// 이 Entity의 변화를 감지하는 리스너를 지정합니다. 여기서는 JPA Auditing 기능을 사용하기 위해 설정했습니다.
@EntityListeners(AuditingEntityListener.class)
// 이 Entity가 실제 어떤 테이블과 매핑될지 이름을 지정합니다. 'user'는 우리가 Workbench에서 만든 테이블 이름입니다.
@Table(name = "user")
public class User {

    // @Id: 이 필드가 테이블의 Primary Key(기본 키)임을 나타냅니다.
    @Id
    // @GeneratedValue: Primary Key의 값을 자동으로 생성하는 방법을 지정합니다.
    // GenerationType.IDENTITY는 데이터베이스의 AUTO_INCREMENT와 동일한 기능입니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column: 이 필드가 테이블의 컬럼과 매핑됨을 나타냅니다.
    // nullable = false는 이 컬럼이 NULL 값을 허용하지 않음을 의미합니다. (NN)
    // unique = true는 이 컬럼의 값이 테이블 내에서 유일해야 함을 의미합니다. (UQ)
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    // @CreatedDate: Entity가 생성될 때의 시간이 자동으로 기록되도록 합니다.
    @CreatedDate
    @Column(updatable = false) // 이 값은 생성된 후 수정되지 않도록 설정합니다.
    private LocalDateTime created_at;

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}