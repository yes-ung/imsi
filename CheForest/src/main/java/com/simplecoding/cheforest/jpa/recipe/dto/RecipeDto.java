package com.simplecoding.cheforest.jpa.recipe.dto;

import com.simplecoding.cheforest.jpa.recipe.entity.Recipe;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
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

    // 조리법 줄 단위 분리
    public List<String> getInstructionSteps() {
        if (instructionKr == null || instructionKr.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(instructionKr.split("\\r?\\n"))
                .filter(s -> !s.isBlank())
                .toList();
    }

    /**
     * 영어 재료와 계량을 조합하여 표시할 List<String> 반환 (컬럼 토글링용)
     */
    public List<String> getIngredientEnDisplayList() {
        // 💡 ingredientEn 필드의 실제 값을 확인합니다.
        System.out.println("DEBUG: ingredientEn RAW -> [" + ingredientEn + "]");

        // 이 조건문이 true가 되어 emptyList()가 반환되는지 확인
        if (ingredientEn == null || ingredientEn.trim().isEmpty()) {
            System.out.println("DEBUG: ingredientEn is considered EMPTY.");
            return Collections.emptyList();
        }

        if (ingredientEn == null || ingredientEn.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String[] ingArr = ingredientEn.split(",");
        String[] meaArr = (measureEn != null && !measureEn.trim().isEmpty())
                ? measureEn.split(",")
                : new String[0];

        List<String> result = new ArrayList<>();

        for (int i = 0; i < ingArr.length; i++) {
            String ing = ingArr[i].trim();
            String mea = (i < meaArr.length) ? meaArr[i].trim() : null;

            if (mea != null && !mea.isEmpty()) {
                // 영어권 포맷: Ingredient (Measure)
                result.add(ing + " (" + mea + ")");
            } else {
                result.add(ing);
            }
        }
        return result;
    }

    /**
     * 영어 조리법을 줄 단위로 분리하여 List<String> 반환 (컬럼 토글링용)
     */
    public List<String> getInstructionEnSteps() {
        System.out.println("DEBUG: instructionEn RAW -> [" + instructionEn + "]"); // <-- 이미 확인됨

        if (instructionEn == null || instructionEn.isBlank()) {
            System.out.println("DEBUG: instructionEn is considered EMPTY.");
            return Collections.emptyList();
        }

        List<String> steps = Arrays.stream(instructionEn.split("\\r?\\n"))
                .map(String::trim) // 💡 공백 제거 추가
                .filter(s -> !s.isEmpty()) // 💡 isBlank() 대신 isEmpty() 사용
                .toList();

        // 💡 최종 List의 크기를 확인합니다.
        System.out.println("DEBUG: instructionEn steps count -> " + steps.size());
        System.out.println("DEBUG: instructionEn first step -> [" + (steps.isEmpty() ? "N/A" : steps.get(0)) + "]");

        return steps;
    }

    public RecipeDto(Recipe entity) {
        this.recipeId = entity.getRecipeId();
        this.titleKr = entity.getTitleKr();
        this.categoryKr = entity.getCategoryKr();
        this.instructionKr = entity.getInstructionKr();
        this.ingredientKr = entity.getIngredientKr();
        this.measureKr = entity.getMeasureKr();
        this.titleEn = entity.getTitleEn();
        this.categoryEn = entity.getCategoryEn();
        this.instructionEn = entity.getInstructionEn();
        this.ingredientEn = entity.getIngredientEn();
        this.measureEn = entity.getMeasureEn();
        this.thumbnail = entity.getThumbnail();
        this.area = entity.getArea();
        this.likeCount = entity.getLikeCount();
        this.viewCount = entity.getViewCount();
        this.difficulty = entity.getDifficulty();
        this.cookTime = entity.getCookTime();
    }
}
