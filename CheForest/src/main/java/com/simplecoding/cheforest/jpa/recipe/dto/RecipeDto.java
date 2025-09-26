package com.simplecoding.cheforest.jpa.recipe.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDto {

    private String recipeId;

    // 한글 정보
    private String titleKr;
    private String categoryKr;
    private String instructionKr;
    private String ingredientKr;
    private String measureKr;

    // 영어 정보
    private String titleEn;
    private String categoryEn;
    private String instructionEn;
    private String ingredientEn;
    private String measureEn;

    // 기타
    private String thumbnail;
    private String area;
    private Long likeCount;
    private Long viewCount;
    private String difficulty; // 난이도 (Easy / Normal / Hard)
    private Integer cookTime;
    // === 편의 메서드 (재료+계량 표시) ===
    public List<String> getIngredientDisplayList() {
        if (ingredientKr == null || ingredientKr.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String[] ingArr = ingredientKr.split(",");
        String[] meaArr = (measureKr != null && !measureKr.trim().isEmpty())
                ? measureKr.split(",")
                : new String[0];

        List<String> result = new ArrayList<>();

        for (int i = 0; i < ingArr.length; i++) {
            String ing = ingArr[i].trim();
            String mea = (i < meaArr.length) ? meaArr[i].trim() : null;

            if (mea != null && !mea.isEmpty()) {
                result.add(ing + " (" + mea + ")");
            } else {
                result.add(ing);
            }
        }

        return result;
    }
}
