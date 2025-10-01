package com.simplecoding.cheforest.jpa.board.entity;

import com.simplecoding.cheforest.jpa.common.BaseTimeEntity;
import com.simplecoding.cheforest.jpa.auth.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BOARD")
@SequenceGenerator(
        name = "BOARD_SEQ_JPA",
        sequenceName = "BOARD_SEQ",
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_JPA")
    private Long boardId;  // 게시글 ID (PK)

    private String category; // 카테고리
    private String title; // 검색대상1
    private String content;
    private String thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_IDX", nullable = false)
    private Member writer;

    private String prepare; // 검색대상2
    @Builder.Default
    private Long viewCount = 0L;
    // 조회수 증가 메소드
    public void increaseViewCount() {
        this.viewCount = (this.viewCount == null ? 1 : this.viewCount + 1);
    }

    @Builder.Default
    private Long likeCount = 0L;

    private Integer cookTime;
    private String difficulty;
    private String prepareAmount;
}
