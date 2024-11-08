package com.wap.wapor.exception;

// 인증 번호 불일치 오류
public class AuthCodeMismatchException extends RuntimeException {
    public AuthCodeMismatchException(String message) {
        super(message);
    }
}
