package com.simplecoding.cheforest.jpa.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewSaveReq {
    private Long boardId;     // 댓글이 달릴 게시글 ID
    private Long writerIdx;   // 작성자 Member ID
    private String content;   // 댓글 내용
}
