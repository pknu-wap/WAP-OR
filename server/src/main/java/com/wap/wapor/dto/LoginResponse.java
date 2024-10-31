package com.wap.wapor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // 모든 필드를 포함한 생성자 생성
public class LoginResponse {
    private String status;
    private String message;
}