package com.wap.wapor.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "likes", uniqueConstraints = {@UniqueConstraint(columnNames = {"paylog_id", "user_id"})}) // 테이블 및 유니크 제약 조건 수정
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // snake_case 매핑
    private Long id; // Primary Key

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paylog_id", nullable = false) // snake_case로 외래 키 매핑
    private PayLog payLog; // 게시글 (FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // snake_case로 외래 키 매핑
    private User user; // 좋아요를 누른 사용자 (FK)
}