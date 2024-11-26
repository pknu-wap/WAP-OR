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
    public VirtualAccount deposit(Long accountId, Long amount, String identifier) {
        // User 존재 확인
        User user = userRepository.findById(identifier)
                .orElseThrow(() -> new IllegalArgumentException("User not found with identifier: " + identifier));

        // VirtualAccount 조회
        VirtualAccount virtualAccount = virtualAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));

        // User와 VirtualAccount 연결 확인
        if (!virtualAccount.getUser().equals(user)) {
            throw new IllegalArgumentException("Virtual account does not belong to the user with identifier: " + identifier);
        }

        // 잔액 업데이트
        virtualAccount.setBalance(virtualAccount.getBalance() + amount);

        // 거래 기록 저장
        Transaction transaction = new Transaction();
        transaction.setVirtualAccount(virtualAccount);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transactionRepository.save(transaction);

        // VirtualAccount 업데이트 저장
        return virtualAccountRepository.save(virtualAccount);
    }
}