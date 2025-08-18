package com.example.Qraft.dto;

import com.example.Qraft.entity.InterviewSet;
import lombok.Getter;

@Getter
public class InterviewSetDto {
    private final Long id;
    private final String name;
    private final String jobType;

    public InterviewSetDto(InterviewSet interviewSet) {
        this.id = interviewSet.getId();
        this.name = interviewSet.getName();
        this.jobType = interviewSet.getJob_type();
    }
}