package com.wap.wapor.controller;


import com.wap.wapor.dto.GetPayLogDto;
import com.wap.wapor.dto.PostPayLogDto;
import com.wap.wapor.security.UserPrincipal;
import com.wap.wapor.service.PayLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.wap.wapor.dto.PayLogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/paylog")
public class PayLogController {
    private final PayLogService payLogService;

    public PayLogController(PayLogService payLogService) {
        this.payLogService = payLogService;
    }

    @PostMapping
    public ResponseEntity<PayLogResponse> createPayLog(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestPart("images") MultipartFile imageFile,@RequestPart("paylog")PostPayLogDto postPayLogDto) {
        PayLogResponse response = payLogService.createPayLog(postPayLogDto, userPrincipal,imageFile);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public List<GetPayLogDto> getPublicPayLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return payLogService.getPublicPayLogs(pageable).getContent();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayLog(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable Long id) {
        try {
            payLogService.deletePayLog(id,userPrincipal);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<GetPayLogDto> getPayLog(@PathVariable Long id) {
        GetPayLogDto getPayLogDto=payLogService.getPayLogDetail(id);
        return ResponseEntity.ok(getPayLogDto);
    }


}


