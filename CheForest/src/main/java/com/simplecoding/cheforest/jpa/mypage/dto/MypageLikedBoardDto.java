package com.simplecoding.cheforest.jpa.mypage.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MypageLikedBoardDto {
    private Long boardId;
    private String title;
    private String writerName;   // nickname
    private LocalDateTime writeDate;
    private Long viewCount;
    private Long likeCount;

    // JPQL에서 사용할 생성자
    public MypageLikedBoardDto(Long boardId, String title, String writerName,
                               LocalDateTime writeDate, Long viewCount, Long likeCount) {
        this.boardId = boardId;
        this.title = title;
        this.writerName = writerName;
        this.writeDate = writeDate;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }
}
