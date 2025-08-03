package com.example.Qraft.dto;

import com.example.Qraft.entity.BoardType;

import lombok.Getter;

// @Getter: 이 클래스의 모든 필드에 대한 getter 메소드를 자동으로 생성합니다.
@Getter
public class PostCreateRequestDto {

    // 서버가 누가 게시글을 썼는지
    private Long userId;

    // 클라이언트로부터 받을 게시글 제목
    private String title;

    // 클라이언트로부터 받을 게시글 내용
    private String content;

    // 클라이언트로부터 받을 게시판 종류 ('INFO' 또는 'FREE')
    private BoardType boardType;
}