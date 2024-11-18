package com.wap.wapor.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId; // 거래 ID (Primary Key)

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private VirtualAccount virtualAccount; // VirtualAccount 테이블과 N:1 관계 (외래 키)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType; // 거래 유형 (DEPOSIT/WITHDRAWAL)

    @Column(nullable = false)
    private Long amount; // 거래 금액

    @Column(nullable = false)
    private LocalDateTime transactionDate; // 거래 발생 시간

    @PrePersist
    protected void onCreate() {
        this.transactionDate = LocalDateTime.now();
    }
}
