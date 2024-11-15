package com.wap.wapor.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Data
public class EmailCheck {
    @Email
    @NotEmpty(message = "이메일을 입력해 주세요")
    private String identifier;

    @NotEmpty(message = "인증 번호를 입력해 주세요")
    private String authCode;
}
