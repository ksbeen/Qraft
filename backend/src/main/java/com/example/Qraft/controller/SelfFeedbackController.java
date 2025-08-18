package com.example.Qraft.controller;

import com.example.Qraft.dto.SelfFeedbackDto;
import com.example.Qraft.service.SelfFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class SelfFeedbackController {

    private final SelfFeedbackService selfFeedbackService;

    // 특정 면접 기록에 대한 모든 피드백을 조회하는 API
    @GetMapping("/{logId}")
    public ResponseEntity<List<SelfFeedbackDto>> getFeedbacksByLogId(@PathVariable Long logId) {
        List<SelfFeedbackDto> feedbacks = selfFeedbackService.findFeedbacksByLogId(logId);
        return ResponseEntity.ok(feedbacks);
    }

    // 새로운 피드백을 생성하는 API
    @PostMapping
    public ResponseEntity<SelfFeedbackDto> createFeedback(@RequestBody SelfFeedbackDto requestDto) {
        SelfFeedbackDto createdFeedback = selfFeedbackService.createFeedback(requestDto);
        return ResponseEntity.ok(createdFeedback);
    }
}