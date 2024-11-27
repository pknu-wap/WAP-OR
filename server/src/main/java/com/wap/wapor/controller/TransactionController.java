package com.wap.wapor.controller;

import com.wap.wapor.domain.Transaction;
import com.wap.wapor.domain.TransactionType;
import com.wap.wapor.repository.TransactionRepository;
import com.wap.wapor.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionRepository transactionRepository;

    @GetMapping("/withdrawals")
    public ResponseEntity<List<Transaction>> getWithdrawalTransactions(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Transaction> withdrawals = transactionRepository.findByVirtualAccount_User_IdentifierAndTransactionType(
                userPrincipal.getId(), TransactionType.WITHDRAWAL);
        return ResponseEntity.ok(withdrawals);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Transaction> allTransactions = transactionRepository.findByVirtualAccount_User_Identifier(userPrincipal.getId());
        return ResponseEntity.ok(allTransactions);
    }
}
