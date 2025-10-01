package com.simplecoding.cheforest.jpa.mypage.dto;

import lombok.Data;

@Data
public class MypageLikedRecipeDto {

    private String recipeId;
    private String titleKr;
    private String categoryKr;
    private Long likeCount;

    // JPQL에서 사용할 생성자
    public MypageLikedRecipeDto(String recipeId, String titleKr, String categoryKr, Long likeCount) {
        this.recipeId = recipeId;
        this.titleKr = titleKr;
        this.categoryKr = categoryKr;
        this.likeCount = likeCount;
    }
}
