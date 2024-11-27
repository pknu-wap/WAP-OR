package com.wap.wapor.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id") // snake_case로 데이터베이스 필드와 매핑
    private Long transactionId; // 거래 ID (Primary Key)

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false) // snake_case로 외래 키 매핑
    private VirtualAccount virtualAccount; // VirtualAccount 테이블과 N:1 관계 (외래 키)

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false) // 거래 유형 필드 매핑
    private TransactionType transactionType; // 거래 유형 (DEPOSIT/WITHDRAWAL)

    @Column(name = "amount", nullable = false) // 거래 금액 매핑
    private Long amount; // 거래 금액

    @Column(name = "transaction_date", nullable = false) // 거래 발생 시간 매핑
    private LocalDateTime transactionDate; // 거래 발생 시간

    @Column(name = "user_nickname", nullable = false) // 사용자 닉네임 추가
    private String userNickname;

    @PrePersist
    protected void onCreate() {
        this.transactionDate = LocalDateTime.now();
    }
}