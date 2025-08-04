package com.example.Qraft.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Qraft.dto.UserSignUpRequestDto;
import com.example.Qraft.service.UserService;

import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;
// @RestController: 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다.
// 이 어노테이션이 붙은 클래스의 메소드들은 기본적으로 JSON 형태로 데이터를 반환합니다.
@RestController
// @RequestMapping: 이 컨트롤러의 모든 메소드에 대한 공통 URL 경로를 설정합니다.
// 즉, 이 컨트롤러의 모든 API 주소는 '/api/users'로 시작합니다.
@RequestMapping("/api/users")
// @RequiredArgsConstructor: final로 선언된 필드를 인자로 받는 생성자를 자동으로 생성합니다. (의존성 주입)
@RequiredArgsConstructor
public class UserController {

    // final 키워드를 사용하여 UserService의 의존성을 주입(DI)받습니다.
    private final UserService userService;

    // @PostMapping: HTTP POST 요청을 처리하는 메소드임을 나타냅니다.
    // '/signup' 경로와 매핑되므로, 최종 URL은 '/api/users/signup'이 됩니다.
    @PostMapping("/signup")
    // @RequestBody: 클라이언트가 보낸 JSON 형식의 요청 본문(body)을
    // UserSignUpRequestDto 객체로 자동으로 변환해줍니다.
    public ResponseEntity<String> signup(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        
        // DTO에서 받은 데이터를 사용하여 UserService의 signup 메소드를 호출합니다.
        userService.signup(
            requestDto.getEmail(),
            requestDto.getPassword(),
            requestDto.getNickname()
        );

        // 회원가입이 성공하면, HTTP 상태 코드 200(OK)와 함께 성공 메시지를 담은 응답을 반환합니다.
        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }
}