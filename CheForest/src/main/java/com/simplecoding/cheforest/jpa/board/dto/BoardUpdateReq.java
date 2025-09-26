package com.simplecoding.cheforest.jpa.board.dto;
import lombok.Data;

@Data
public class BoardUpdateReq {
    private Long boardId;
    private String category;
    private String title;
    private String prepare;
    private String content;
    private String thumbnail;
    private Integer cookTime;
    private String difficulty;
}
