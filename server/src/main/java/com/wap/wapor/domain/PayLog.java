package com.wap.wapor.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PayLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // snake_case와 매핑
    private Long id; // Primary Key

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // snake_case로 외래 키 매핑
    private User user; // 작성자 정보 (FK)

    @Column(name = "content") // snake_case로 매핑
    private String content; // 게시글 내용

    @Column(name = "title") // snake_case로 매핑
    private String title; // 게시글 제목

    @Column(name = "category") // snake_case로 매핑
    private String category; // 카테고리

    @Column(name = "amount") // snake_case로 매핑
    private Long amount; // 금액
    @Column(name = "isPublic")
    private boolean isPublic;
    @Column(name = "created_at", nullable = false, updatable = false) // snake_case로 매핑
    private LocalDateTime createdAt; // 작성 시간

    @Column(name = "img_url") // snake_case로 매핑
    private String imgUrl; // 첨부 이미지 URL

    @Column(name = "like_count") // snake_case로 매핑
    private int likeCount = 0; // 좋아요 수 (캐싱)

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); // 작성 시간 기본값 설정
    }
}