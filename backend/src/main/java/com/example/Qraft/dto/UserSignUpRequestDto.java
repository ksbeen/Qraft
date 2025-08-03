package com.example.Qraft.dto;

import lombok.Getter;

// @Getter: 이 클래스의 모든 필드에 대한 getter 메소드를 자동으로 생성해줍니다.
@Getter
public class UserSignUpRequestDto {

    // 클라이언트로부터 받을 'email' 필드
    private String email;

    // 클라이언트로부터 받을 'password' 필드
    private String password;

    // 클라이언트로부터 받을 'nickname' 필드
    private String nickname;
}