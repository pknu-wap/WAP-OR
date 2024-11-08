package com.wap.wapor.controller;

import com.wap.wapor.dto.EmailCheck;
import com.wap.wapor.dto.EmailLoginRequest;
import com.wap.wapor.exception.AuthCodeMismatchException;
import com.wap.wapor.service.EmailSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailSendService emailService;

    // 이메일 인증 번호 전송
    @PostMapping("/mailSend")
    public ResponseEntity<String> mailSend(@RequestBody @Valid EmailLoginRequest emailRequest) {
        String authCode = emailService.joinEmail(emailRequest.getIdentifier());  // 인증 번호 전송
        return ResponseEntity.ok("인증 번호가 전송되었습니다. 인증 번호: " + authCode);
    }

    // 인증 번호 확인
    @PostMapping("/mailAuthCheck")
    public ResponseEntity<String> authCheck(@RequestBody @Valid EmailCheck emailCheck) {
        emailService.verifyAuthCode(emailCheck.getAuthCode());
        return ResponseEntity.ok("인증 번호가 일치합니다.");
    }
}