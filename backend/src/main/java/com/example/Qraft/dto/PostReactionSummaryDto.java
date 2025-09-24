package com.example.Qraft.dto;

import com.example.Qraft.entity.PostReaction;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostReactionSummaryDto {
    private final Long postId;
    private final Long recommendCount;
    private final Long opposeCount;
    private final PostReaction.ReactionType currentUserReaction;
    
    public PostReactionSummaryDto(Long postId, Long recommendCount, Long opposeCount) {
        this(postId, recommendCount, opposeCount, null);
    }
}
