package com.example.Qraft.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Qraft.entity.User;
import com.example.Qraft.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// @Service: 이 클래스가 비즈니스 로직을 처리하는 'Service' 클래스임을 Spring 프레임워크에 알려줍니다.
@Service
// @RequiredArgsConstructor: final로 선언된 필드를 인자로 받는 생성자를 자동으로 생성해줍니다. (의존성 주입)
@RequiredArgsConstructor
public class UserService {

    // final 키워드를 사용하여 UserRepository의 의존성을 주입(DI)받습니다.
    // 이 Service 클래스는 이제 UserRepository의 메소드들을 사용할 수 있습니다.
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 암호화 도구 주입

    /**
     * 회원가입 기능을 처리하는 메소드
     * @param email 사용자의 이메일
     * @param password 사용자의 비밀번호
     * @param nickname 사용자의 닉네임
     * @return 저장된 User 객체
     */
    // @Transactional: 이 메소드에서 수행되는 모든 DB 작업이 하나의 트랜잭션으로 묶입니다.
    // 작업 중 하나라도 실패하면 모든 변경 사항이 롤백(취소)됩니다.
    @Transactional
    public User signup(String email, String password, String nickname) {
        
        
        // 위에서 추가한 @Builder를 사용하여 User 객체를 생성합니다.
        User newUser = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password)) // 비밀번호를 암호화하여 저장합니다.
                .nickname(nickname)
                .build();

        // userRepository의 save 메소드를 호출하여 사용자를 데이터베이스에 저장합니다.
        return userRepository.save(newUser);
    }
}