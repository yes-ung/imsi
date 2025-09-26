package com.simplecoding.cheforest.jpa.recipe.service;

import com.simplecoding.cheforest.jpa.common.MapStruct;
import com.simplecoding.cheforest.jpa.recipe.dto.RecipeCardDTO;
import com.simplecoding.cheforest.jpa.recipe.dto.RecipeDto;
import com.simplecoding.cheforest.jpa.recipe.entity.Recipe;
import com.simplecoding.cheforest.jpa.recipe.repository.RecipeRepository;
import com.simplecoding.cheforest.jpa.recipe.util.ImageDownloader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final MapStruct mapStruct;
    private final ImageDownloader imageDownloader;

    // 1. 페이징 + 검색
    public Page<RecipeDto> getRecipeList(String categoryKr, String titleKr, Pageable pageable) {

        String keyword = (titleKr == null) ? "" : titleKr.trim();
        boolean hasCategory = StringUtils.hasText(categoryKr);

        Page<Recipe> page;
        if (hasCategory) {
            // ✅ 카테고리는 정확일치, 제목은 부분일치(대소문자 무시)
            page = recipeRepository.findByCategoryKrAndTitleKrContainingIgnoreCase(
                    categoryKr.trim(), keyword, pageable);
        } else {
            // ✅ 전체: 제목만 부분일치
            page = recipeRepository.findByTitleKrContainingIgnoreCase(keyword, pageable);
        }

        return page.map(mapStruct::toDto);
    }

    public long countAllRecipes() {
        return recipeRepository.count();
    }

    // 2. 상세 조회
    public RecipeDto getRecipeDetail(String recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피 없음: " + recipeId));
        return mapStruct.toDto(recipe);
    }

    // 3. 랜덤 레시피 조회
    // 3. 랜덤 레시피 조회
    public List<RecipeDto> getRandomRecipes(String categoryKr, int count) {
        // ✅ Repository에서 랜덤 전체 불러온 후 Service에서 개수 제한
        return recipeRepository.findRandomByCategory(categoryKr)
                .stream()
                .limit(count)
                .map(mapStruct::toDto)
                .toList();
    }

    public List<RecipeCardDTO> getPopularTop4() {
        return recipeRepository
                .findTop4ByLikeCountGreaterThanOrderByLikeCountDescViewCountDescRecipeIdDesc(0L)
                .stream()
                .map(r -> RecipeCardDTO.builder()
                        .id(r.getRecipeId())
                        .title(r.getTitleKr())
                        .thumbnail(r.getThumbnail())
                        .categoryName(r.getCategoryKr())
                        .writerNickname("CheForest 관리자")   // API데이터: 작성자 없음 → 고정값
                        .cookTime(r.getCookTime())
                        .difficulty(r.getDifficulty())
                        .viewCount(r.getViewCount())
                        .likeCount(r.getLikeCount())
                        .build()
                )
                .toList();
    }

    // 4. 인기 레시피 TOP10
    public List<RecipeDto> getBest3Recipes() {
        return mapStruct.toDtoList(recipeRepository.findTop3ByOrderByLikeCountDescRecipeIdDesc());
    }


    // 5. 썸네일 다운로드 & 로컬 캐싱
    public void downloadAndCacheAllImages() {
        List<Recipe> recipes = recipeRepository.findAll();

        log.info("🔁 총 {}개의 레시피 썸네일 처리 시작", recipes.size());

        for (Recipe recipe : recipes) {
            String recipeId = recipe.getRecipeId();
            String imageUrl = recipe.getThumbnail();

            if (imageUrl == null || imageUrl.isBlank()) {
                log.warn("❌ [{}] 무시됨 - URL 없음", recipeId);
                continue;
            }

            try {
                String localPath = "images/recipes/" + recipeId;
                imageDownloader.downloadImage(imageUrl, localPath);

                // DB 업데이트
                recipe.setThumbnail("/" + localPath + ".jpg");
                recipeRepository.save(recipe);

                log.info("✅ [{}] 처리 완료 - DB에 썸네일 경로 업데이트", recipeId);
            } catch (Exception e) {
                log.error("❌ [{}] 다운로드 실패 - URL: {}", recipeId, imageUrl, e);
            }
        }

        log.info("✅ 전체 썸네일 처리 완료");
    }
    // 6. 카테고리 목록 조회
    public List<String> getAllCategories() {
        return recipeRepository.findDistinctCategories();
    }
    // 7. 카테고리별 레시피 개수
    public Map<String, Long> getCategoryCounts() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream()
                .collect(Collectors.groupingBy(Recipe::getCategoryKr, Collectors.counting()));
    }
    @Transactional
    public void viewCount(String recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피 없음: " + recipeId));

        recipe.setViewCount(recipe.getViewCount() + 1);
        recipeRepository.save(recipe);
    }
}
