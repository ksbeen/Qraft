package com.example.Qraft.service;

import com.example.Qraft.dto.PracticeLogRequestDto;
import com.example.Qraft.entity.InterviewSet;
import com.example.Qraft.entity.Practice_Log;
import com.example.Qraft.entity.User;
import com.example.Qraft.repository.InterviewSetRepository;
import com.example.Qraft.repository.PracticeLogRepository;
import com.example.Qraft.repository.UserRepository;
import com.example.Qraft.dto.PracticeLogResponseDto;
import com.example.Qraft.dto.PracticeLogDetailResponseDto; 
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; 
import java.util.stream.Collectors; 

/**
 * @Service: 이 클래스가 서비스 계층의 컴포넌트(Bean)임을 Spring에게 알려줍니다.
 * @RequiredArgsConstructor: final로 선언된 필드에 대한 생성자를 자동으로 만들어, 의존성 주입을 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class PracticeLogService {

    // Spring의 의존성 주입(DI) 컨테이너에 의해 주입되는 Repository들
    private final PracticeLogRepository practiceLogRepository;
    private final UserRepository userRepository;
    private final InterviewSetRepository interviewSetRepository;

    /**
     * 새로운 면접 연습 기록을 생성하고 데이터베이스에 저장합니다.
     * @param userEmail 현재 로그인된 사용자의 이메일
     * @param requestDto interviewSetId와 videoUrl을 담고 있는 DTO
     * @return 새로 저장된 Practice_Log 엔티티
     */
    @Transactional
    public Practice_Log createLog(String userEmail, PracticeLogRequestDto requestDto) {
        // 1. JWT에서 추출한 이메일을 기반으로 User 엔티티를 찾습니다.
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        // 2. 요청 DTO에 담겨온 ID를 기반으로 InterviewSet 엔티티를 찾습니다.
        InterviewSet interviewSet = interviewSetRepository.findById(requestDto.getInterviewSetId())
                .orElseThrow(() -> new IllegalArgumentException("면접 세트를 찾을 수 없습니다."));

        // 3. Builder 패턴을 사용하여 새로운 Practice_Log 엔티티를 생성합니다.
        Practice_Log newLog = Practice_Log.builder()
                .video_url(requestDto.getVideoUrl())
                .user(user)
                .interviewSet(interviewSet)
                .build();
        
        // 4. 새로 생성한 엔티티를 데이터베이스에 저장하고 반환합니다.
        return practiceLogRepository.save(newLog);
    }
    @Transactional(readOnly = true)
    public List<PracticeLogResponseDto> findMyLogs(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return practiceLogRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(PracticeLogResponseDto::new)
                .collect(Collectors.toList());
    }
    /**
     * ID로 특정 면접 기록을 조회하는 메소드
     * @param logId 조회할 면접 기록의 ID
     * @return 면접 기록 상세 정보 DTO
     */
    @Transactional(readOnly = true)
    public PracticeLogDetailResponseDto findLogById(Long logId) {
        // ID로 Practice_Log 엔티티를 찾고, 없으면 예외를 발생시킵니다.
        return practiceLogRepository.findById(logId)
                // 찾은 엔티티를 DTO로 변환하여 반환합니다.
                .map(PracticeLogDetailResponseDto::new)
                .orElseThrow(() -> new IllegalArgumentException("해당 면접 기록을 찾을 수 없습니다. id=" + logId));
    }
}