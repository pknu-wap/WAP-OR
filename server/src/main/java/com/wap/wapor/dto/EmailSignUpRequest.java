package com.wap.wapor.dto;

import lombok.Data;

@Data
public class EmailSignUpRequest {
    private String password;
    private String identifier;
}
