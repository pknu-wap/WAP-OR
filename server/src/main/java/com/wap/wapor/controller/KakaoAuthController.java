package com.wap.wapor.controller;

import com.wap.wapor.domain.User;
import com.wap.wapor.dto.AuthResponse;
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
    public ResponseEntity<AuthResponse> kakaoLogin(@RequestHeader("Authorization") String authorizationHeader) {
        // "Bearer " 부분을 제거하고 실제 액세스 토큰만 추출
        String accessToken = authorizationHeader.replace("Bearer ", "");
        AuthResponse authResponse = kakaoAuthService.processKakaoLogin(accessToken);
        return ResponseEntity.ok(authResponse);
    }
}