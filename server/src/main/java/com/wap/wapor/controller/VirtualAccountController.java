package com.wap.wapor.controller;

import com.wap.wapor.domain.VirtualAccount;
import com.wap.wapor.dto.DepositRequest;
import com.wap.wapor.dto.DepositResponse;
import com.wap.wapor.service.VirtualAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/virtual-account")
@RequiredArgsConstructor
public class VirtualAccountController {

    private final VirtualAccountService virtualAccountService;

    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> deposit(@Valid @RequestBody DepositRequest request) {
        VirtualAccount updatedAccount = virtualAccountService.deposit(
                request.getAccountId(),
                request.getAmount(),
                request.getIdentifier()
        );

        DepositResponse response = new DepositResponse();
        response.setAccountId(updatedAccount.getAccountId());
        response.setNewBalance(updatedAccount.getBalance());

        return ResponseEntity.ok(response);
    }
}