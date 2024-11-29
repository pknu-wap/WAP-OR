package com.wap.wapor.service;

import com.wap.wapor.domain.Transaction;
import com.wap.wapor.domain.TransactionType;
import com.wap.wapor.domain.User;
import com.wap.wapor.domain.VirtualAccount;
import com.wap.wapor.repository.TransactionRepository;
import com.wap.wapor.repository.UserRepository;
import com.wap.wapor.repository.VirtualAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VirtualAccountService {

    private final VirtualAccountRepository virtualAccountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public VirtualAccount deposit(Long amount, String identifier) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("입금 금액은 0보다 커야 합니다.");
        }

        // User 존재 확인
        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new IllegalArgumentException("User not found with identifier: " + identifier));

        // User와 연결된 VirtualAccount 조회
        VirtualAccount virtualAccount = virtualAccountRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("No virtual account found for user: " + identifier));

        // 잔액 업데이트
        virtualAccount.setBalance(virtualAccount.getBalance() + amount);

        // 거래 기록 저장
        Transaction transaction = new Transaction();
        transaction.setVirtualAccount(virtualAccount);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setBalance(virtualAccount.getBalance());
        transaction.setPayLog(null);
        transaction.setCategory("사용자 입금");

        transactionRepository.save(transaction);

        // VirtualAccount 업데이트 저장
        return virtualAccountRepository.save(virtualAccount);
    }

    @Transactional(readOnly = true)
    public Long getBalance(String identifier) {
        // User 존재 확인
        User user = userRepository.findByIdentifier(identifier)
                .orElseThrow(() -> new IllegalArgumentException("User not found with identifier: " + identifier));

        // User와 연결된 VirtualAccount 조회
        VirtualAccount virtualAccount = virtualAccountRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("No virtual account found for user: " + identifier));

        // 현재 계좌 잔액 반환
        return virtualAccount.getBalance();
    }
}