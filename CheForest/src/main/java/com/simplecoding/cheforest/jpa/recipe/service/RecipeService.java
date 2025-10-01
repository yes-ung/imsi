package com.simplecoding.cheforest.jpa.recipe.service;

import com.simplecoding.cheforest.jpa.recipe.dto.RecipeDto;
import com.simplecoding.cheforest.jpa.recipe.entity.Recipe;
import com.simplecoding.cheforest.jpa.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    // 1. 레시피 목록 조회 (카테고리 + 검색 + 검색타입 + 페이징)
    public Page<RecipeDto> getRecipeList(String categoryKr, String searchKeyword, String searchType, Pageable pageable) {
        Page<Recipe> recipePage;

        // 1. 검색 키워드가 있는 경우
        if (!searchKeyword.isEmpty()) {
            if ("ingredient".equalsIgnoreCase(searchType)) {
                // 1-1. 재료 검색
                if (!categoryKr.isEmpty()) {
                    // 카테고리 + 재료 검색
                    recipePage = recipeRepository.findByCategoryKrAndIngredientKrContainingIgnoreCase(
                            categoryKr, searchKeyword, pageable);
                } else {
                    // 전체 + 재료 검색
                    recipePage = recipeRepository.findByIngredientKrContainingIgnoreCase(searchKeyword, pageable);
                }
            } else { // 기본값: "title" 검색
                // 1-2. 제목 검색
                if (!categoryKr.isEmpty()) {
                    // 카테고리 + 제목 검색
                    recipePage = recipeRepository.findByCategoryKrAndTitleKrContainingIgnoreCase(
                            categoryKr, searchKeyword, pageable);
                } else {
                    // 전체 + 제목 검색
                    recipePage = recipeRepository.findByTitleKrContainingIgnoreCase(
                            searchKeyword, pageable);
                }
            }
        } else {
            // 2. 검색 키워드가 없는 경우 (카테고리 필터링 또는 전체 목록)
            if (!categoryKr.isEmpty()) {
                // 카테고리만 (전체 검색어 없음)
                recipePage = recipeRepository.findByCategoryKr(categoryKr, pageable);
            } else {
                // 전체 (카테고리, 검색어 모두 없음)
                recipePage = recipeRepository.findAll(pageable);
            }
        }

        // Entity Page를 DTO Page로 변환
        // 🚨 [수정] 메서드 레퍼런스 대신 람다로 변경하여 타입 추론 오류 해결
        return recipePage.map(recipe -> new RecipeDto(recipe));
    }

    // 2. 레시피 상세 조회
    public RecipeDto getRecipeDetail(String recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 레시피가 없습니다. id=" + recipeId));
        return new RecipeDto(recipe);
    }

    // 3. 카테고리별 개수 맵 조회
    public Map<String, Long> getCategoryCounts() {
        return recipeRepository.countRecipesByCategory().stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0], // categoryKr
                        arr -> (Long) arr[1]    // count
                ));
    }

    // 4. 전체 레시피 총 개수
    public long countAllRecipes() {
        return recipeRepository.count();
    }

    // 5. 인기 레시피 (TOP 3)
    public List<RecipeDto> getBest3Recipes() {
        // 좋아요 1개 이상 기준으로 TOP 3 선정
        List<Recipe> recipes = recipeRepository.findTop3ByOrderByLikeCountDescRecipeIdDesc();
        return recipes.stream().map(RecipeDto::new).collect(Collectors.toList());
    }

    // 6. 모든 카테고리 목록 조회
    public List<String> getAllCategories() {
        return recipeRepository.findDistinctCategories();
    }

    // 7. 조회수 +1
    @Transactional
    public void viewCount(String recipeId) {
        recipeRepository.findById(recipeId).ifPresent(Recipe::addViewCount);
    }

    // 8. 미세먼지 좋은 음식 랜덤 조회 (count는 Service에서 처리)
    public List<RecipeDto> getRandomDustGood(int count) {
        return recipeRepository.findRandomDustGood().stream()
                .limit(count) // Service에서 limit 적용
                .map(recipe -> new RecipeDto(recipe)) // 람다 사용
                .collect(Collectors.toList());
    }

    // 9. 특정 카테고리의 랜덤 레시피 조회 (DustMapController에서 호출)
    public List<RecipeDto> getRandomRecipesByCategory(String categoryKr, int count) {
        return recipeRepository.findRandomByCategory(categoryKr).stream()
                .limit(count) // Service에서 limit 적용
                .map(recipe -> new RecipeDto(recipe)) // 람다 사용
                .collect(Collectors.toList());
    }

    // 10. 이미지 캐싱 로직 (생략)
    public void downloadAndCacheAllImages() {
        // 로직 생략
    }
}
