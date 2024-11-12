package com.wap.wapor.dto;

import com.wap.wapor.domain.UserType;
import lombok.Data;

@Data
public class EmailSignUpRequest {
    private UserType userType;
    private String password;
    private String identifier;
}