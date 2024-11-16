package com.wap.wapor.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "user_id"})})
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // like_id (Primary Key)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paylog_id", nullable = false)
    private PayLog payLog; // 게시글 (FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 좋아요를 누른 사용자 (FK)

}
