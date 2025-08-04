package com.example.Qraft.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice: 모든 @RestController에서 발생하는 예외를
// 이 클래스에서 처리하도록 지정합니다.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler: 특정 예외(Exception)를 잡아서 처리할 메소드를 지정합니다.
    // 여기서는 IllegalArgumentException이 발생하면 이 메소드가 실행됩니다.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {

        // 예외가 발생하면, HTTP 상태 코드 404 (Not Found)와 함께
        // 예외 메시지("해당 게시글을 찾을 수 없습니다...")를 응답 본문에 담아 반환합니다.
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 검증 실패 시, DTO에 설정된 default message를 응답 본문에 담아 반환합니다.
        String errorMessage = ex.getBindingResult()
                                .getAllErrors()
                                .get(0)
                                .getDefaultMessage();
        
        // 400 Bad Request 상태 코드와 함께 에러 메시지를 반환합니다.
        return ResponseEntity.status(400).body(errorMessage);
    }
}