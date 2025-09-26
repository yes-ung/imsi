package com.simplecoding.cheforest.jpa.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUpdateReq {
    private Long reviewId;    // 수정할 댓글 ID
    private Long boardId;     // 게시글 ID (삭제/수정 시 같이 전달됨)
    private String content;   // 수정된 내용
}
