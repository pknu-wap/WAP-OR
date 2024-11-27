package com.wap.wapor.repository;

import com.wap.wapor.domain.Transaction;
import com.wap.wapor.domain.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // 특정 사용자의 모든 거래 내역 조회
    List<Transaction> findByVirtualAccount_User_Identifier(String identifier);

    // 특정 사용자의 출금 내역만 조회
    List<Transaction> findByVirtualAccount_User_IdentifierAndTransactionType(String identifier, TransactionType transactionType);
}