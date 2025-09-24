package com.example.Qraft.dto;

import java.time.LocalDateTime;

import com.example.Qraft.entity.BoardType;
import com.example.Qraft.entity.Post;
import com.example.Qraft.entity.PostReaction;

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
    // 클라이언트에게 보여줄 게시판 타입
    private final BoardType boardType;
    // 클라이언트에게 보여줄 조회수
    private final Integer views;
    // 클라이언트에게 보여줄 작성자 ID
    private final Long authorId;
    // 클라이언트에게 보여줄 작성자 닉네임
    private final String authorNickname;
    // 클라이언트에게 보여줄 생성일
    private final LocalDateTime createdAt;
    // 추천 수
    private final Long recommendCount;
    // 비추천 수
    private final Long opposeCount;
    // 현재 사용자의 반응 (없으면 null)
    private final PostReaction.ReactionType currentUserReaction;

    /**
     * Post Entity 객체를 인자로 받아서 PostResponseDto를 생성하는 생성자입니다.
     * Entity 객체에서 필요한 정보만 뽑아서 DTO의 필드를 채웁니다.
     * @param post 조회된 Post Entity 객체
     */
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.boardType = post.getBoard_type();
        this.views = post.getViews();
        this.authorId = post.getUser().getId(); // 연관된 User 객체에서 ID를 가져옵니다.
        this.authorNickname = post.getUser().getNickname(); // 연관된 User 객체에서 닉네임을 가져옵니다.
        this.createdAt = post.getCreated_at();
        this.recommendCount = 0L;
        this.opposeCount = 0L;
        this.currentUserReaction = null;
    }

    /**
     * 추천/비추천 정보를 포함한 생성자
     */
    public PostResponseDto(Post post, Long recommendCount, Long opposeCount, PostReaction.ReactionType currentUserReaction) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.boardType = post.getBoard_type();
        this.views = post.getViews();
        this.authorId = post.getUser().getId();
        this.authorNickname = post.getUser().getNickname();
        this.createdAt = post.getCreated_at();
        this.recommendCount = recommendCount;
        this.opposeCount = opposeCount;
        this.currentUserReaction = currentUserReaction;
    }
}