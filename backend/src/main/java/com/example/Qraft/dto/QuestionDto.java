package com.example.Qraft.dto;

import com.example.Qraft.entity.Question;
import lombok.Getter;

@Getter
public class QuestionDto {
    private final Long id;
    private final String content;
    private final Integer sequence;

    public QuestionDto(Question question) {
        this.id = question.getId();
        this.content = question.getContent();
        this.sequence = question.getSequence();
    }
}