package com.wap.wapor.controller;

import com.wap.wapor.dto.PayLogResponse;
import com.wap.wapor.dto.PostPayLogDto;
import com.wap.wapor.security.UserPrincipal;
import com.wap.wapor.service.PayLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paylog")
public class PayLogController {
    private final PayLogService payLogService;

    public PayLogController(PayLogService payLogService) {
        this.payLogService = payLogService;
    }

    @PostMapping
    public ResponseEntity<PayLogResponse> createPayLog(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody PostPayLogDto postPayLogDto) {
        PayLogResponse response = payLogService.createPayLog(postPayLogDto, userPrincipal);
        return ResponseEntity.ok(response);
    }
}