package com.example.Qraft.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Qraft.dto.LoginRequestDto;
import com.example.Qraft.dto.TokenDto;
import com.example.Qraft.dto.UserResponseDto;
import com.example.Qraft.dto.UserSignUpRequestDto;
import com.example.Qraft.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @RestController: 이 클래스가 REST API의 요청을 처리하는 컨트롤러임을 나타냅니다.
 * 각 메소드의 반환 값은 자동으로 JSON 형태로 변환되어 HTTP 응답 본문에 담깁니다.
 * @RequestMapping("/api/users"): 이 컨트롤러 내의 모든 API는 '/api/users' 라는 공통된 URL 경로를 가집니다.
 * @RequiredArgsConstructor: final 필드에 대한 생성자 주입을 자동으로 처리해줍니다.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    // 비즈니스 로직을 처리하는 UserService를 주입받습니다.
    private final UserService userService;

    /**
     * 회원가입 API (/api/users/signup)
     * @param requestDto 클라이언트가 보낸 회원가입 정보를 담은 DTO
     * @return 성공 메시지 문자열
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        // UserService의 signup 메소드를 호출하여 회원가입 로직을 수행합니다.
        userService.signup(
            requestDto.getEmail(),
            requestDto.getPassword(),
            requestDto.getNickname()
        );
        // 성공 시, HTTP 상태 코드 200(OK)와 함께 성공 메시지를 응답으로 보냅니다.
        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }

    /**
     * 로그인 API (/api/users/login)
     * @param requestDto 클라이언트가 보낸 로그인 정보를 담은 DTO (이메일, 비밀번호)
     * @return 로그인 성공 시 발급된 JWT를 담은 DTO
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto requestDto) {
        // 1. UserService의 login 메소드를 호출하여 JWT 문자열을 받습니다.
        String token = userService.login(requestDto);
        // 2. 받은 JWT를 TokenDto로 감싸서 HTTP 응답 본문에 담아 보냅니다.
        return ResponseEntity.ok(new TokenDto(token));
    }

    /**
     * 현재 로그인한 사용자 정보 조회 API (/api/users/me)
     * @param authentication Spring Security에서 제공하는 인증 정보
     * @return 현재 사용자의 정보를 담은 DTO
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication) {
        // 인증된 사용자의 이메일을 통해 사용자 정보를 조회합니다.
        UserResponseDto userResponse = userService.getCurrentUser(authentication.getName());
        return ResponseEntity.ok(userResponse);
    }
}