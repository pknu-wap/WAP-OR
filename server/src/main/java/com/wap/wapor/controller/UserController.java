package com.wap.wapor.controller;

import com.wap.wapor.dto.AuthResponse;
import com.wap.wapor.dto.EmailLoginRequest;
import com.wap.wapor.dto.EmailSignUpRequest;
import com.wap.wapor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 이메일 회원가입
    @PostMapping("/register/email")
    public ResponseEntity<String> registerUser(@RequestBody EmailSignUpRequest signUpRequest) {
        return userService.registerUser(signUpRequest.getIdentifier(), signUpRequest.getPassword());
    }

    // 이메일 로그인
    @PostMapping("/login/email")
    public ResponseEntity<AuthResponse> emailLogin(@RequestBody EmailLoginRequest emailLoginRequest) {
        // UserService에서 반환된 ResponseEntity를 그대로 반환
        return userService.authenticateUser(emailLoginRequest);
    }
}