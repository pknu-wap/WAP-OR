package com.wap.wapor.controller;

import com.wap.wapor.domain.Transaction;
import com.wap.wapor.domain.TransactionType;
import com.wap.wapor.dto.TransactionResponse;
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

    // 출금 내역 조회
    @GetMapping("/withdrawals")
    public ResponseEntity<List<TransactionResponse>> getWithdrawalTransactions(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<TransactionResponse> withdrawals = transactionRepository.findByVirtualAccount_User_IdentifierAndTransactionType(
                        userPrincipal.getId(), TransactionType.WITHDRAWAL
                ).stream()
                .map(this::mapToTransactionResponse)
                .toList();
        return ResponseEntity.ok(withdrawals);
    }

    // 입금 내역 조회
    @GetMapping("/deposits")
    public ResponseEntity<List<TransactionResponse>> getDepositTransactions(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<TransactionResponse> deposits = transactionRepository.findByVirtualAccount_User_IdentifierAndTransactionType(
                        userPrincipal.getId(), TransactionType.DEPOSIT
                ).stream()
                .map(this::mapToTransactionResponse)
                .toList();
        return ResponseEntity.ok(deposits);
    }

    // 모든 거래 내역 조회
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<TransactionResponse> allTransactions = transactionRepository.findByVirtualAccount_User_Identifier(userPrincipal.getId())
                .stream()
                .map(this::mapToTransactionResponse)
                .toList();
        return ResponseEntity.ok(allTransactions);
    }

    // Transaction -> TransactionResponse 변환
    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getTransactionDate(), // 거래 발생 시간
                transaction.getCategory(),        // Transaction 테이블의 카테고리
                transaction.getAmount(),          // 거래 금액
                transaction.getBalance()          // 거래 시점의 잔액
        );
    }
}
