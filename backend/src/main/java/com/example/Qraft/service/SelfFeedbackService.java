package com.example.Qraft.service;

import com.example.Qraft.dto.SelfFeedbackDto;
import com.example.Qraft.entity.Practice_Log;
import com.example.Qraft.entity.Self_Feedback;
import com.example.Qraft.repository.PracticeLogRepository;
import com.example.Qraft.repository.SelfFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SelfFeedbackService {

    private final SelfFeedbackRepository selfFeedbackRepository;
    private final PracticeLogRepository practiceLogRepository;

    // 특정 면접 기록에 대한 모든 피드백을 조회하는 로직
    @Transactional(readOnly = true)
    public List<SelfFeedbackDto> findFeedbacksByLogId(Long logId) {
        return selfFeedbackRepository.findByPracticeLogIdOrderByVideoTimestampAsc(logId)
                .stream()
                .map(SelfFeedbackDto::new)
                .collect(Collectors.toList());
    }

    // 새로운 피드백을 생성하는 로직
    @Transactional
    public SelfFeedbackDto createFeedback(SelfFeedbackDto requestDto) {
        Practice_Log practiceLog = practiceLogRepository.findById(requestDto.getPracticeLogId())
                .orElseThrow(() -> new IllegalArgumentException("면접 기록을 찾을 수 없습니다."));

        Self_Feedback newFeedback = Self_Feedback.builder()
                .practiceLog(practiceLog)
                .videoTimestamp(requestDto.getVideoTimestamp())
                .memo(requestDto.getMemo())
                .build();

        Self_Feedback savedFeedback = selfFeedbackRepository.save(newFeedback);
        return new SelfFeedbackDto(savedFeedback);
    }
}