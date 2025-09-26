package com.simplecoding.cheforest.jpa.recipe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "API_RECIPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    private String recipeId;   // PK

    // 한글 정보
    private String titleKr;

    private String categoryKr;

    @Lob
    private String instructionKr;

    private String ingredientKr;

    private String measureKr;

    // 영어 정보
    private String titleEn;

    private String categoryEn;

    @Lob
    private String instructionEn;

    private String ingredientEn;

    private String measureEn;

    // 기타
    private String thumbnail;

    private String area;

    private Long likeCount;

    @Column(nullable = false)
    private Long viewCount = 0L;

    private Integer cookTime;
    private String difficulty;
}

