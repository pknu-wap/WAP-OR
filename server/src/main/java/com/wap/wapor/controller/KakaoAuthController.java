package com.wap.wapor.controller;

import com.wap.wapor.domain.User;
import com.wap.wapor.domain.UserType;
import com.wap.wapor.dto.AuthResponse;
import com.wap.wapor.repository.UserRepository;
import com.wap.wapor.security.UserPrincipal;
import com.wap.wapor.service.KakaoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class KakaoAuthController {
    private final KakaoAuthService kakaoAuthService;
    private final UserRepository userRepository;
    public KakaoAuthController(KakaoAuthService kakaoAuthService, UserRepository userRepository) {
        this.kakaoAuthService = kakaoAuthService;
        this.userRepository = userRepository;
    }

    @PostMapping("/kakao")
    public ResponseEntity<AuthResponse> kakaoLogin(@RequestHeader("Authorization") String authorizationHeader) {
        // "Bearer " 부분을 제거하고 실제 액세스 토큰만 추출
        String accessToken = authorizationHeader.replace("Bearer ", "");
        AuthResponse authResponse = kakaoAuthService.processKakaoLogin(accessToken);
        return ResponseEntity.ok(authResponse);
    }
    @GetMapping("/user/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userRepository
                .findByIdentifierAndUserType(userPrincipal.getId(), UserType.KAKAO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}