package com.wap.wapor.controller;

import com.wap.wapor.domain.User;
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
    public ResponseEntity<User> kakaoLogin(@RequestHeader("Authorization") String authorizationHeader) {
        // "Bearer " 부분을 제거하고 실제 액세스 토큰만 추출
        String accessToken = authorizationHeader.replace("Bearer ", "");
        User user = kakaoAuthService.processKakaoLogin(accessToken);
        return ResponseEntity.ok(user);
    }
}