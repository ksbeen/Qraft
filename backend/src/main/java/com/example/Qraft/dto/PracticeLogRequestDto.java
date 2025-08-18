package com.example.Qraft.dto;

import lombok.Getter;

/**
 * @Getter: Lombok 어노테이션으로, 모든 필드에 대한 getter 메소드를 자동으로 생성합니다.
 * 이 DTO는 새로운 면접 연습 기록을 생성하기 위한 데이터를 전달하는 데 사용됩니다.
 */
@Getter
public class PracticeLogRequestDto {
    // 연습한 면접 세트의 ID
    private Long interviewSetId;

    // 녹화된 영상의 URL 또는 파일명
    private String videoUrl;
}