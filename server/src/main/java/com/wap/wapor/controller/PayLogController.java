package com.wap.wapor.controller;

import com.wap.wapor.dto.GetPayLogDto;
import com.wap.wapor.dto.PostPayLogDto;
import com.wap.wapor.security.UserPrincipal;
import com.wap.wapor.service.PayLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @GetMapping("/search")
    public Page<GetPayLogDto> getPublicPayLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return payLogService.getPublicPayLogs(pageable);
    }

}
