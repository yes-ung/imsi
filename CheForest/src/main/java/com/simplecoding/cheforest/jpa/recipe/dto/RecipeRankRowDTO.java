package com.simplecoding.cheforest.jpa.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeRankRowDTO {
    private String id;            // RECIPE_ID
    private String title;         // TITLE_KR
    private String categoryName;  // CATEGORY_KR
    private String writerNickname;// API는 표시용 고정 "CheForest"
    private Long likeCount;       // LIKE_COUNT
    private Long viewCount;       // VIEW_COUNT
}