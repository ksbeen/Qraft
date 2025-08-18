/* DTO는 클라이언트에게 보낼 상세 데이터를 담는 그릇입니다. */
package com.example.Qraft.dto;

import com.example.Qraft.entity.Practice_Log;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PracticeLogDetailResponseDto {
    private final Long id;
    private final String videoUrl;
    private final LocalDateTime createdAt;
    private final String interviewSetName;

    public PracticeLogDetailResponseDto(Practice_Log practiceLog) {
        this.id = practiceLog.getId();
        this.videoUrl = practiceLog.getVideo_url();
        this.createdAt = practiceLog.getCreatedAt();
        this.interviewSetName = practiceLog.getInterviewSet().getName();
    }
}