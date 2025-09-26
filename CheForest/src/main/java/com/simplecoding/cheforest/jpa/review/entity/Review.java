package com.simplecoding.cheforest.jpa.review.entity;

import com.simplecoding.cheforest.jpa.common.BaseTimeEntity;
import com.simplecoding.cheforest.jpa.board.entity.Board;
import com.simplecoding.cheforest.jpa.auth.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BOARD_REVIEW")
@SequenceGenerator(
        name = "BOARD_REVIEW_SEQ_JPA",
        sequenceName = "BOARD_REVIEW_SEQ",  // 실제 DB 시퀀스 이름
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_REVIEW_SEQ_JPA")
    private Long reviewId;   // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_BOARD_REVIEW_BOARD"))
    private Board board;     // 게시글 참조

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_IDX", nullable = false,
            foreignKey = @ForeignKey(name = "FK_BOARD_REVIEW_MEMBER"))
    private Member writer;   // 작성자 참조

    private String content;  // 댓글 내용
}
