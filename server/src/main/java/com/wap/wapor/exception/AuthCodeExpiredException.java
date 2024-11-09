package com.wap.wapor.exception;

public class AuthCodeExpiredException extends RuntimeException {
    public AuthCodeExpiredException(String message) {
        super(message);
    }
}