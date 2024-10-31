package com.wap.wapor.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType; // 사용자의 유형(KAKAO/EMAIL)

    @Column(nullable = false, unique = true)
    private String identifier; // Kakao ID or Email address

    @Column(nullable = true) // 카카오 회원은 null
    private String password;

    @Column(nullable = false) // 모든 회원에게 필수
    private String nickname;

    @Column(nullable = true) // 카카오 회원의 리프레시 토큰
    private String refreshToken; // 카카오 API를 통해 다시 로그인할 때 사용

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 사용자가 처음 생성된 시간 저장

    private LocalDateTime lastLogin; // 사용자가 마지막으로 로그인한 시간 저장

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastLogin = LocalDateTime.now();
    }
}