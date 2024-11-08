package com.wap.wapor.exception;

// 메일 전송 오류
public class CustomMailSendException extends RuntimeException {
    public CustomMailSendException(String message) {
        super(message);
    }

    public CustomMailSendException(String message, Throwable cause) {
        super(message, cause);
    }
}