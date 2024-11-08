package com.wap.wapor.exception;

// 이메일 유효성 오류
public class EmailValidationException extends RuntimeException {
    public EmailValidationException(String message) {
        super(message);
    }
}
