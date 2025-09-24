package com.example.Qraft.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 댓글 요청 DTO
 */
public class CommentRequestDto {
    
    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(max = 1000, message = "댓글은 1000자 이하로 작성해주세요.")
    private String content;
    
    // 기본 생성자
    public CommentRequestDto() {}
    
    // 생성자
    public CommentRequestDto(String content) {
        this.content = content;
    }
    
    // Getter and Setter
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
}
