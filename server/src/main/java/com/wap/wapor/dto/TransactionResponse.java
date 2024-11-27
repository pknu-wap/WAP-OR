package com.wap.wapor.dto;

import com.wap.wapor.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private LocalDateTime transactionDate; // 거래 발생 시간
    private String category;              // 페이로그 카테고리
    private Long amount;                  // 거래 금액
    private Long balance;                 // 잔액
}

