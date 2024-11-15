package com.wap.wapor.dto;

import com.wap.wapor.domain.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {                 //토큰과 유저정보를 담은 dto
    // Getters and Setters
    private String token;
    private User user;

    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

}
