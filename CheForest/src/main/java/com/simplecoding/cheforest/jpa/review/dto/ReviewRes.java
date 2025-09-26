package com.simplecoding.cheforest.jpa.review.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewRes {
    private Long reviewId;        // 댓글 ID
    private Long boardId;         // 게시글 ID
    private Long writerIdx;       // 작성자 ID
    private String nickname;      // 작성자 닉네임
    private String content;       // 댓글 내용
    private LocalDateTime insertTime; // 작성일
    private LocalDateTime updateTime; // 수정일
}
