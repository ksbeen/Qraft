package com.example.Qraft.dto;

import java.time.LocalDateTime;

import com.example.Qraft.entity.Post;

import lombok.Getter;

// @Getter: 필드에 대한 getter 메소드를 자동으로 생성합니다.
@Getter
public class PostResponseDto {

    // 클라이언트에게 보여줄 게시글 ID
    private final Long id;
    // 클라이언트에게 보여줄 게시글 제목
    private final String title;
    // 클라이언트에게 보여줄 게시글 내용
    private final String content;
    // 클라이언트에게 보여줄 작성자 닉네임
    private final String authorNickname;
    // 클라이언트에게 보여줄 생성일
    private final LocalDateTime createdAt;

    /**
     * Post Entity 객체를 인자로 받아서 PostResponseDto를 생성하는 생성자입니다.
     * Entity 객체에서 필요한 정보만 뽑아서 DTO의 필드를 채웁니다.
     * @param post 조회된 Post Entity 객체
     */
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorNickname = post.getUser().getNickname(); // 연관된 User 객체에서 닉네임을 가져옵니다.
        this.createdAt = post.getCreated_at();
    }
}