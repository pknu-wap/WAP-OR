package com.wap.wapor.controller;

import com.wap.wapor.entity.User;
import com.wap.wapor.service.KakaoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class KakaoAuthController {
    private final KakaoAuthService kakaoAuthService;

    public KakaoAuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @PostMapping("/kakao")
    public ResponseEntity<User> kakaoLogin(@RequestBody String accessToken) {
        User user = kakaoAuthService.processKakaoLogin(accessToken);
        return ResponseEntity.ok(user);
    }
}