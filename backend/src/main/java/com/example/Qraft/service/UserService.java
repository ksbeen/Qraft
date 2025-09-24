package com.example.Qraft.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Qraft.dto.LoginRequestDto;
import com.example.Qraft.dto.UserResponseDto;
import com.example.Qraft.entity.User;
import com.example.Qraft.jwt.JwtTokenProvider;
import com.example.Qraft.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * @Service: 이 클래스가 비즈니스 로직을 담당하는 서비스 계층임을 Spring에게 알려줍니다.
 * @RequiredArgsConstructor: final로 선언된 필드들을 기반으로 생성자를 자동으로 만들어줍니다.
 * 이를 통해 우리는 생성자 주입(Constructor Injection) 방식으로 의존성을 주입받게 됩니다.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    // final: 이 필드는 생성 시점에 반드시 초기화되어야 하며, 변경될 수 없음을 의미합니다.
    // UserRepository: User 엔티티에 대한 데이터베이스 작업을 처리합니다.
    private final UserRepository userRepository;
    // PasswordEncoder: 비밀번호를 안전하게 암호화(해싱)하고, 입력된 비밀번호와 비교하는 역할을 합니다.
    private final PasswordEncoder passwordEncoder;
    // JwtTokenProvider: 로그인 성공 시 JWT를 생성하는 역할을 합니다.
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입 비즈니스 로직을 처리하는 메소드
     * @param email 사용자가 입력한 이메일
     * @param password 사용자가 입력한 비밀번호
     * @param nickname 사용자가 입력한 닉네임
     * @return 데이터베이스에 저장된 User 객체
     */
    @Transactional
    public User signup(String email, String password, String nickname) {
        
        // 1. User 객체를 생성합니다.
        User newUser = User.builder()
                .email(email)
                // 2. 비밀번호는 반드시 암호화하여 저장해야 합니다.
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .build();

        // 3. userRepository를 통해 User 객체를 데이터베이스에 저장(INSERT)합니다.
        return userRepository.save(newUser);
    }

    /**
     * 로그인 비즈니스 로직을 처리하는 메소드
     * @param requestDto 사용자가 입력한 이메일과 비밀번호
     * @return 로그인 성공 시 발급되는 JWT(JSON Web Token)
     */
    @Transactional(readOnly = true)
    public String login(LoginRequestDto requestDto) {
        
        // 1. 입력된 이메일을 기반으로 DB에서 사용자를 찾습니다.
        //    - .orElseThrow(): 사용자를 찾지 못했을 경우, 지정된 예외를 발생시켜 메소드를 즉시 중단합니다.
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
        
        // 2. 입력된 비밀번호(평문)와 DB에 저장된 비밀번호(암호화된 값)를 비교합니다.
        //    - passwordEncoder.matches()는 평문과 암호화된 값이 일치하는지 확인해줍니다.
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 3. 모든 인증 절차를 통과하면, 해당 사용자를 위한 JWT를 생성하여 반환합니다.
        return jwtTokenProvider.createToken(user);
    }

    /**
     * 현재 로그인한 사용자의 정보를 조회하는 메소드
     * @param email 인증된 사용자의 이메일
     * @return 사용자 정보를 담은 DTO
     */
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        return new UserResponseDto(user.getId(), user.getEmail(), user.getNickname());
    }
}