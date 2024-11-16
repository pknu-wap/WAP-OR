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
    private Long id; // post_id (Primary Key)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 작성자 정보 (FK)

    private String content; // 게시글 내용
    private String title;   //게시글 제목
    private String category; // 카테고리

    private Double amount; // 금액

    private LocalDateTime createdAt; // 작성 시간

    private String imageUrl; // 첨부 이미지 URL

    private int likeCount = 0; // 좋아요 수 (캐싱)

    @OneToMany(mappedBy = "payLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes; // 좋아요 리스트
}

