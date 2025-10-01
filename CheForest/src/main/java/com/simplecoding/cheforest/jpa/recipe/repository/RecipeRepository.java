package com.simplecoding.cheforest.jpa.recipe.repository;

import com.simplecoding.cheforest.jpa.recipe.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, String> {

    // ✅ 1-1. 전체 + 제목 검색: 제목만 부분일치
    Page<Recipe> findByTitleKrContainingIgnoreCase(String titleKr, Pageable pageable);

    // 🌟 [추가] 1-2. 카테고리 지정 단독: 카테고리만 정확일치 (검색어 없을 때 사용)
    Page<Recipe> findByCategoryKr(String categoryKr, Pageable pageable);

    // ✅ 1-3. 카테고리 + 제목 검색: 카테고리는 정확일치, 제목은 부분일치
    Page<Recipe> findByCategoryKrAndTitleKrContainingIgnoreCase(
            String categoryKr,
            String titleKr,
            Pageable pageable
    );

    // 🌟 [추가] 1-4. 카테고리 + 재료 검색: 카테고리는 정확일치, 재료는 부분일치
    Page<Recipe> findByCategoryKrAndIngredientKrContainingIgnoreCase(
            String categoryKr,
            String ingredientKr,
            Pageable pageable
    );

    // 🌟 [추가] 1-5. 전체 + 재료 검색: 재료만 부분일치
    Page<Recipe> findByIngredientKrContainingIgnoreCase(String ingredientKr, Pageable pageable);


    // 2. 랜덤 조회 (Oracle RANDOM → JPA 네이티브 쿼리 사용)
    @Query(value = """
            SELECT *
            FROM API_RECIPE
            WHERE CATEGORY_KR = ?1
            ORDER BY DBMS_RANDOM.VALUE
            """, nativeQuery = true)
    List<Recipe> findRandomByCategory(String categoryKr);
    // ------------------------- 👆 기존 구조 그대로 유지 --------------------------

    // 3. 썸네일만 전체 조회
    @Query("SELECT r.recipeId, r.thumbnail FROM Recipe r")
    List<Object[]> findAllRecipeThumb();

    // 4. 좋아요 0 초과 TOP4(홈화면용)
    List<Recipe> findTop4ByLikeCountGreaterThanOrderByLikeCountDescViewCountDescRecipeIdDesc(long minLikeCount);

    // 5. 전체 인기 레시피 (TOP 3 by LIKE_COUNT)
    List<Recipe> findTop3ByOrderByLikeCountDescRecipeIdDesc();

    // 5-1. 카테고리별 TOP3
    List<Recipe> findTop3ByCategoryKrAndLikeCountGreaterThanOrderByLikeCountDescViewCountDescRecipeIdDesc(String categoryKr, long minLikeCount);

    // 6. 카테고리 목록 (중복 제거)
    @Query("SELECT DISTINCT r.categoryKr FROM Recipe r ORDER BY r.categoryKr")
    List<String> findDistinctCategories();

    // 7. 카테고리별 레시피 개수
    @Query("SELECT r.categoryKr, COUNT(r) FROM Recipe r GROUP BY r.categoryKr")
    List<Object[]> countRecipesByCategory();
    // 8. 미세먼지 좋은 음식 (dust_good = 'Y') 랜덤 조회
    @Query(value = """
        SELECT *
        FROM API_RECIPE
        WHERE DUST_GOOD = 'Y'
        ORDER BY DBMS_RANDOM.VALUE
        """, nativeQuery = true)
    List<Recipe> findRandomDustGood();

    // 9. 제철 재료 레시피 검색(미완성)
    List<Recipe> findBySeasonIsNotNullAndTitleKrContainingIgnoreCase(String keyword);

}


