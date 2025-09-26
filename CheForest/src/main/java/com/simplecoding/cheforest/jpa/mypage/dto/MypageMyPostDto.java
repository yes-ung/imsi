package com.simplecoding.cheforest.jpa.mypage.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MypageMyPostDto {

    private Long boardId;
    private String title;
    private LocalDateTime insertTime;  // LocalDateTime 으로 변경
    private Long viewCount;
    private Long likeCount;

    // JPQL에서 사용할 생성자
    public MypageMyPostDto(Long boardId, String title, LocalDateTime writeDate, Long viewCount, Long likeCount) {
        this.boardId = boardId;
        this.title = title;
        this.insertTime = insertTime;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }
}
