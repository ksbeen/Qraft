package com.example.Qraft.controller;

import com.example.Qraft.dto.PracticeLogRequestDto;
import com.example.Qraft.service.PracticeLogService;
import com.example.Qraft.dto.PracticeLogResponseDto;
import com.example.Qraft.dto.PracticeLogDetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @RestController: 이 클래스가 RESTful 컨트롤러임을 나타냅니다.
 * @RequestMapping: 이 컨트롤러의 모든 API에 대한 기본 URL 경로를 설정합니다.
 * @RequiredArgsConstructor: 생성자 기반의 의존성 주입을 활성화합니다.
 */
@RestController
@RequestMapping("/api/practice-logs")
@RequiredArgsConstructor
public class PracticeLogController {

    private final PracticeLogService practiceLogService;

    /**
     * 새로운 면접 연습 기록을 생성하는 API 엔드포인트
     * @param requestDto interviewSetId와 videoUrl을 담고 있는 요청 본문(body)
     * @return 성공 메시지
     */
    @PostMapping
    public ResponseEntity<String> createPracticeLog(@RequestBody PracticeLogRequestDto requestDto) {
        // 1. SecurityContextHolder에서 현재 사용자의 인증 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 2. 인증 정보에서 사용자의 이메일(우리 시스템에서는 'name'에 해당)을 추출합니다.
        String userEmail = authentication.getName();
        
        // 3. 서비스를 호출하여 비즈니스 로직을 수행합니다.
        practiceLogService.createLog(userEmail, requestDto);
        
        // 4. 성공 응답을 반환합니다.
        return ResponseEntity.ok("면접 기록이 성공적으로 저장되었습니다.");
    }
    @GetMapping("/my")
    public ResponseEntity<List<PracticeLogResponseDto>> getMyLogs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<PracticeLogResponseDto> myLogs = practiceLogService.findMyLogs(userEmail);
        return ResponseEntity.ok(myLogs);
    }
    /**
     * ID로 특정 면접 기록을 조회하는 API
     * @param id URL 경로에서 추출한 면접 기록 ID
     * @return 면접 기록 상세 정보 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<PracticeLogDetailResponseDto> getLogById(@PathVariable Long id) {
        PracticeLogDetailResponseDto logDetail = practiceLogService.findLogById(id);
        return ResponseEntity.ok(logDetail);
    }
}