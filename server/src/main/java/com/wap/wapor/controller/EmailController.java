package com.wap.wapor.controller;

import com.wap.wapor.dto.EmailCheck;
import com.wap.wapor.dto.EmailSignUpRequest;
import com.wap.wapor.service.EmailSendService;
import com.wap.wapor.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailSendService emailService;
    private final RedisUtil redisUtil;

    // 이메일 인증 번호 전송
    @PostMapping("/mailSend")
    public ResponseEntity<String> mailSend(@RequestBody @Valid EmailSignUpRequest emailRequest) {
        String authCode = emailService.requestAuthCode(emailRequest.getIdentifier());  // 인증 번호 전송
        return ResponseEntity.ok("인증 번호가 전송되었습니다. 인증 번호: " + authCode);
    }

    // 인증 번호 확인 및 인증 상태 저장
    @PostMapping("/mailAuthCheck")
    public ResponseEntity<String> authCheck(@RequestBody @Valid EmailCheck emailCheck) {
        emailService.verifyAuthCode(emailCheck.getIdentifier(), emailCheck.getAuthCode());
        redisUtil.setDataExpire("verified:" + emailCheck.getIdentifier(), "true"); // 인증 성공 시 상태 저장 (TTL 설정 가능)
        return ResponseEntity.ok("인증 번호가 일치합니다.");
    }
}