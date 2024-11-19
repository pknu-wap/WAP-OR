package com.wap.wapor.controller;

import com.wap.wapor.dto.PostPayLogDto;
import com.wap.wapor.security.UserPrincipal;
import com.wap.wapor.service.PayLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paylog")
public class PayLogController {
    private final PayLogService payLogService;

    public PayLogController(PayLogService payLogService) {
        this.payLogService = payLogService;
    }

    @PostMapping
    public ResponseEntity<Long> createPayLog(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody PostPayLogDto postPayLogDto) {
       Long postId= payLogService.payLogPost(postPayLogDto, userPrincipal);
       return ResponseEntity.ok(postId);

    }

}
