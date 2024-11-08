package com.wap.wapor.controller;

import com.wap.wapor.dto.EmailCheck;
import com.wap.wapor.dto.EmailLoginRequest;
import com.wap.wapor.service.EmailSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailSendService emailService;

    // 이메일 인증 번호 전송
    @PostMapping("/mailSend")
    public ResponseEntity<?> mailSend(@RequestBody @Valid EmailLoginRequest emailRequest) {
        System.out.println("이메일 인증 요청이 들어옴");
        System.out.println("이메일 인증 이메일:" + emailRequest.getIdentifier());

        try {
            String response = emailService.joinEmail(emailRequest.getIdentifier());
            return ResponseEntity.ok("인증번호가 전송되었습니다: " + response); // 확인용
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 중 오류가 발생했습니다.");
        }
    }

    // 인증 번호 확인
    @PostMapping("/mailAuthCheck")
    public ResponseEntity<String> authCheck(@RequestBody @Valid EmailCheck emailCheck) {
        boolean isValid = emailService.verifyAuthCode(emailCheck.getAuthCode());

        if (isValid) {
            return ResponseEntity.ok("인증번호가 일치합니다.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증번호가 일치하지 않습니다.");
        }
    }
}
