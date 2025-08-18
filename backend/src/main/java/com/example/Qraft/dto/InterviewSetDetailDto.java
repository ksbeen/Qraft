package com.example.Qraft.dto;

import com.example.Qraft.entity.InterviewSet;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class InterviewSetDetailDto {
    private final Long id;
    private final String name;
    private final String jobType;
    // 이 면접 세트에 포함된 모든 질문 목록을 담을 리스트
    private final List<QuestionDto> questions;

    public InterviewSetDetailDto(InterviewSet interviewSet) {
        this.id = interviewSet.getId();
        this.name = interviewSet.getName();
        this.jobType = interviewSet.getJob_type();
        // InterviewSet 엔티티에 있는 Question 엔티티 리스트를
        // QuestionDto 리스트로 변환하여 저장합니다.
        this.questions = interviewSet.getQuestions().stream()
                .map(QuestionDto::new)
                .collect(Collectors.toList());
    }
}