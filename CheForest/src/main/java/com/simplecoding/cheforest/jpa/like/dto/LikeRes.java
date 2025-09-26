package com.simplecoding.cheforest.jpa.like.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeRes {     // 좋아요 응답 (조회/출력 컨트롤러용)
    private Long likeId;
    private String likeType;  // BOARD / RECIPE
    private Long boardId;
    private String recipeId;
    private Long likeCount;    // 게시글/레시피 좋아요 수
    private LocalDateTime likeDate;
}
