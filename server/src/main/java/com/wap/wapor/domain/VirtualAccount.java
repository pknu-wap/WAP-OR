package com.wap.wapor.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class VirtualAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId; // 계좌 ID (Primary Key)

    @Column(nullable = false)
    private Long balance; // 잔액 (기본값 0)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt; // 생성 시간

    @Column(nullable = true)
    private LocalDateTime updateAt; // 갱신 시간

    @OneToOne
    @JoinColumn(name = "userId", nullable = false, unique = true)
    private User user; // User 테이블과 1:1 관계 (외래 키)

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.balance = 0L; // 기본 잔액 설정
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}