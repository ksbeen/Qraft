package com.example.Qraft.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자를 만들어줍니다.
public class TokenDto {
    private String token;
}