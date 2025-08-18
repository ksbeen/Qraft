package com.example.Qraft.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @RestControllerAdvice: 모든 컨트롤러(@RestController)에서 발생하는 예외(Exception)를
 * 전역적으로 잡아서 처리하는 클래스임을 나타냅니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @ExceptionHandler(IllegalArgumentException.class):
     * Service 계층에서 데이터가 존재하지 않는 등 비즈니스 로직 상의 예외가 발생했을 때 이 메소드가 실행됩니다.
     * @return 404 Not Found 상태 코드와 예외 메시지를 응답으로 보냅니다.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    /**
     * @ExceptionHandler(MethodArgumentNotValidException.class):
     * DTO에 @Valid 어노테이션으로 설정한 유효성 검증에 실패했을 때 이 메소드가 실행됩니다.
     * @return 400 Bad Request 상태 코드와 DTO에 설정된 메시지를 응답으로 보냅니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                                .getAllErrors()
                                .get(0)
                                .getDefaultMessage();
        
        return ResponseEntity.status(400).body(errorMessage);
    }
    
    /**
     * @ExceptionHandler(AccessDeniedException.class):
     * Service 계층에서 권한(Authorization) 검사에 실패했을 때 이 메소드가 실행됩니다.
     * @return 403 Forbidden 상태 코드와 예외 메시지를 응답으로 보냅니다.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(403).body(ex.getMessage());
    }
}