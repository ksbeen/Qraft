package com.example.Qraft.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Qraft.dto.InterviewSetDetailDto;
import com.example.Qraft.dto.InterviewSetDto;
import com.example.Qraft.service.InterviewService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/interview-sets")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    // GET /api/interview-sets
    @GetMapping
    public ResponseEntity<List<InterviewSetDto>> getAllInterviewSets() {
        List<InterviewSetDto> interviewSets = interviewService.findAllInterviewSets();
        return ResponseEntity.ok(interviewSets);
    }

    // GET /api/interview-sets/{id}
    @GetMapping("/{id}")
    public ResponseEntity<InterviewSetDetailDto> getInterviewSetById(@PathVariable Long id) {
        InterviewSetDetailDto interviewSetDetail = interviewService.findInterviewSetById(id);
        return ResponseEntity.ok(interviewSetDetail);
    }
}