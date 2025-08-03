package com.example.Qraft.dto;

import lombok.Getter;

@Getter
public class PostUpdateRequestDto {

    // 클라이언트로부터 받을 새로운 게시글 제목
    private String title;

    // 클라이언트로부터 받을 새로운 게시글 내용
    private String content;
}