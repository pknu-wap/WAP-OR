package com.wap.wapor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. 메일 전송 오류 처리
    @ExceptionHandler(CustomMailSendException.class)
    public ResponseEntity<String> handleCustomMailSendException(CustomMailSendException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("메일 전송 오류: " + ex.getMessage());
    }

    // 2. 이메일 유효성 오류 처리
    @ExceptionHandler(EmailValidationException.class)
    public ResponseEntity<String> handleEmailValidationException(EmailValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 유효성 오류: " + ex.getMessage());
    }

    // 3. 인증번호 불일치 오류 처리
    @ExceptionHandler(AuthCodeMismatchException.class)
    public ResponseEntity<String> handleAuthCodeMismatchException(AuthCodeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 번호 오류: " + ex.getMessage());
    }

    // 4. 인증번호 만료 오류 처리 (제한 시간 만료)
    @ExceptionHandler(AuthCodeExpiredException.class)
    public ResponseEntity<String> handleAuthCodeExpiredException(AuthCodeExpiredException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body("제한 시간 오류: " + ex.getMessage());
    }

    // 5. 이메일이 존재하지 않을 때의 오류 처리
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<String> handleEmailNotFoundException(EmailNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이메일 조회 오류: " + ex.getMessage());
    }

    // 6. Redis 서버 연결 오류 처리
    @ExceptionHandler(RedisConnectionException.class)
    public ResponseEntity<String> handleRedisConnectionException(RedisConnectionException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Redis 서버 연결 오류: " + ex.getMessage());
    }

    // 7. 이미 존재하는 사용자 예외 처리
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("회원 가입 오류: " + ex.getMessage());
    }

    // 8. 잘못된 자격 증명 예외 처리
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 오류: " + ex.getMessage());
    }

    // 9. 이메일 인증 상태 오류 처리
    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<String> handleEmailNotVerifiedException(EmailNotVerifiedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이메일 인증 오류: " + ex.getMessage());
    }

    // 10. 비밀번호 유효성 오류 처리
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 오류: " + ex.getMessage());
    }


    // 그 외 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + ex.getMessage());
    }
}