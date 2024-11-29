package com.wap.wapor.controller;

import com.wap.wapor.domain.VirtualAccount;
import com.wap.wapor.dto.DepositRequest;
import com.wap.wapor.dto.DepositResponse;
import com.wap.wapor.security.UserPrincipal;
import com.wap.wapor.service.VirtualAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/virtual-account")
@RequiredArgsConstructor
public class VirtualAccountController {

    private final VirtualAccountService virtualAccountService;

    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> deposit(
            @Valid @RequestBody DepositRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        VirtualAccount updatedAccount = virtualAccountService.deposit(
                request.getAmount(),
                userPrincipal.getId() // 인증된 사용자 식별자 전달
        );

        DepositResponse response = new DepositResponse();
        response.setAccountId(updatedAccount.getAccountId());
        response.setBalance(updatedAccount.getBalance());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/balance")
    public ResponseEntity<Long> getBalance(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long balance = virtualAccountService.getBalance(userPrincipal.getId());
        return ResponseEntity.ok(balance); // 잔액만 반환
    }
}