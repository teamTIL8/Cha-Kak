package com.chakak.exception;

import com.chakak.dto.response.AuthErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * DTO 유효성 검사 실패 시 발생하는 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AuthErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.error("Validation failed: {}", errorMessage);
        return ResponseEntity.badRequest().body(new AuthErrorResponseDto(errorMessage, "VALIDATION_ERROR"));
    }

    /**
     * 비즈니스 로직 예외 처리 (예: 중복 아이디/이메일, 사용자 없음 등)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AuthErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Business logic error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(new AuthErrorResponseDto(ex.getMessage(), "BUSINESS_ERROR"));
    }

    /**
     * Spring Security의 UsernameNotFoundException 처리
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<AuthErrorResponseDto> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error("Authentication error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthErrorResponseDto(ex.getMessage(), "UNAUTHORIZED"));
    }

    /**
     * Optional.orElseThrow() 등으로 요소가 없을 때 발생하는 예외 처리
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<AuthErrorResponseDto> handleNoSuchElementException(NoSuchElementException ex) {
        log.error("Data not found error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AuthErrorResponseDto(ex.getMessage(), "NOT_FOUND"));
    }

    /**
     * 그 외 모든 예상치 못한 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthErrorResponseDto> handleAllExceptions(Exception ex) {
        log.error("An unexpected error occurred: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthErrorResponseDto("서버 오류가 발생했습니다.", "INTERNAL_SERVER_ERROR"));
    }
}
