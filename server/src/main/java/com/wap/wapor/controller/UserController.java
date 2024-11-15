package com.wap.wapor.controller;

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

    @PostMapping("/register/email")
    public ResponseEntity<String> registerUser(@RequestBody EmailSignUpRequest signUpRequest) {
        return userService.registerUser(signUpRequest.getIdentifier(), signUpRequest.getPassword());
    }

    @PostMapping("/login/email")
    public ResponseEntity<String> emailLogin(@RequestBody EmailLoginRequest emailLoginRequest) {
        return userService.authenticateUser(emailLoginRequest);
    }
}