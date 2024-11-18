package com.wap.wapor.controller;

import com.wap.wapor.domain.VirtualAccount;
import com.wap.wapor.dto.DepositRequest;
import com.wap.wapor.service.VirtualAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/virtual-account")
@RequiredArgsConstructor
public class VirtualAccountController {

    private final VirtualAccountService virtualAccountService;

    @PostMapping("/deposit")
    public ResponseEntity<VirtualAccount> deposit(@RequestBody DepositRequest request) {
        VirtualAccount updatedAccount = virtualAccountService.deposit(
                request.getAccountId(),
                request.getAmount(),
                request.getIdentifier()
        );
        return ResponseEntity.ok(updatedAccount);
    }
}