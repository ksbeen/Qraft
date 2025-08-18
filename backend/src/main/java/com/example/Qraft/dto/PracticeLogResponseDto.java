package com.example.Qraft.dto;

import com.example.Qraft.entity.Practice_Log;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PracticeLogResponseDto {
    private final Long id;
    private final String videoUrl;
    private final LocalDateTime createdAt;
    private final String interviewSetName; // 면접 세트의 이름을 포함

    public PracticeLogResponseDto(Practice_Log practiceLog) {
        this.id = practiceLog.getId();
        this.videoUrl = practiceLog.getVideo_url();
        this.createdAt = practiceLog.getCreatedAt();
        this.interviewSetName = practiceLog.getInterviewSet().getName();
    }
}