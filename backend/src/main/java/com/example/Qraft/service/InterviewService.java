package com.example.Qraft.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Qraft.dto.InterviewSetDetailDto; // DTO import 추가
import com.example.Qraft.dto.InterviewSetDto;
import com.example.Qraft.repository.InterviewSetRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewSetRepository interviewSetRepository;

    /**
     * 모든 면접 세트 목록을 조회하는 메소드
     * @return 면접 세트 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<InterviewSetDto> findAllInterviewSets() {
        return interviewSetRepository.findAll()
                .stream()
                .map(InterviewSetDto::new)
                .collect(Collectors.toList());
    }

    // ▼▼▼▼▼ 이 메소드가 누락되었습니다 ▼▼▼▼▼
    /**
     * ID로 특정 면접 세트와 질문 목록을 조회하는 메소드
     * @param setId 조회할 면접 세트의 ID
     * @return 면접 세트 상세 정보 DTO
     */
    @Transactional(readOnly = true)
    public InterviewSetDetailDto findInterviewSetById(Long setId) {
        // 1. ID로 InterviewSet 엔티티를 찾습니다. 없으면 예외를 발생시킵니다.
        return interviewSetRepository.findById(setId)
                // 2. 찾은 엔티티를 InterviewSetDetailDto로 변환하여 반환합니다.
                .map(InterviewSetDetailDto::new)
                .orElseThrow(() -> new IllegalArgumentException("해당 면접 세트를 찾을 수 없습니다. id=" + setId));
    }
}