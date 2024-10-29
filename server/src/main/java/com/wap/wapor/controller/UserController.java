package com.wap.wapor.controller;
import com.wap.wapor.dto.EmailLoginRequest;
import com.wap.wapor.service.UserService;
import com.wap.wapor.domain.UserType;
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

    /*

    @PostMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoLoginRequest kakaoLoginRequest) {
        if (kakaoLoginRequest.getUserType() != UserType.KAKAO) {
            return userService.kakaoLogin(kakaoLoginRequest);
        }
    }

     */

    @PostMapping("/login/email")
    public ResponseEntity<?> emailLogin(@RequestBody EmailLoginRequest emailLoginRequest) {
        if (emailLoginRequest.getUserType() != UserType.EMAIL) {
            return ResponseEntity.badRequest().body("Invalid user type for Email login");
        }
        return userService.emailLogin(emailLoginRequest);
    }
}
