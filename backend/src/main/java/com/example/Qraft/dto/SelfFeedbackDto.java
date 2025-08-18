// src/dto/SelfFeedbackDto.java
package com.example.Qraft.dto;

import com.example.Qraft.entity.Self_Feedback;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SelfFeedbackDto {
    private Long id;
    private Integer videoTimestamp;
    private String memo;
    private Long practiceLogId;

    public SelfFeedbackDto(Self_Feedback feedback) {
        this.id = feedback.getId();
        // Lombok이 자동으로 만들어주는 getter 이름도 getVideoTimestamp()로 변경되었으므로,
        // 이 부분도 함께 수정합니다.
        this.videoTimestamp = feedback.getVideoTimestamp();
        this.memo = feedback.getMemo();
    }
}