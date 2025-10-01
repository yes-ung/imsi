//package com.simplecoding.cheforest.jpa.recipe.controller;
//
//import com.simplecoding.cheforest.jpa.recipe.dto.RecipeDto;
//import com.simplecoding.cheforest.jpa.recipe.service.RecipeService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@Controller
//@RequestMapping("/recipe")
//@RequiredArgsConstructor
//public class RecipeController {
//
//    private final RecipeService recipeService;
//
//    // ✅ 1. 이미지 캐싱 실행 (관리자 전용)
//    @PostMapping("/cache-images")
//    @ResponseBody
//    public String cacheImages() {
//        recipeService.downloadAndCacheAllImages();
//        return "✅ 이미지 캐싱 완료!";
//    }
//
//    // ✅ 2. 레시피 목록 조회 (카테고리 + 검색 + 페이징 + 전체총합)
//    @GetMapping("/list")
//    public String showRecipeList(
//            @RequestParam(defaultValue = "") String categoryKr,
//            @RequestParam(defaultValue = "") String searchKeyword,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "9") int size,
//            Model model) {
//
//        Pageable pageable = PageRequest.of(page, size);
//
//        // (필터 반영) 현재 페이지 데이터
//        Page<RecipeDto> recipePage = recipeService.getRecipeList(categoryKr, searchKeyword, pageable);
//
//        // 카테고리별 개수 맵  {"한식":77, "양식":87, ...}
//        Map<String, Long> recipeCountMap = recipeService.getCategoryCounts();
//
//        // ✅ 전체 총합(필터와 무관, ‘전체’ 뱃지에 쓰는 값)
//        long allTotalCount = recipeService.countAllRecipes();
//
//        // (선택) 사이드바 노출 순서 고정(추후 수정)
//        List<String> categoryOrder = java.util.Arrays.asList("한식","양식","중식","일식","디저트");
//
//        model.addAttribute("recipeList", recipePage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", recipePage.getTotalPages());
//        model.addAttribute("categoryKr", categoryKr);
//        model.addAttribute("searchKeyword", searchKeyword);
//
//        // 상단 “전체 레시피 n개”는 현재 필터/검색 기준
//        model.addAttribute("totalCount", recipePage.getTotalElements());
//
//        // ✅ 새로 추가
//        model.addAttribute("allTotalCount", allTotalCount);
//        model.addAttribute("recipeCountMap", recipeCountMap);
//        model.addAttribute("categoryOrder", categoryOrder);
//
//        // 기존
//        model.addAttribute("best3Recipes", recipeService.getBest3Recipes());
//        model.addAttribute("categoryList", recipeService.getAllCategories());
//
//        return "recipe/recipelist";
//    }
//
//    // ✅ 3. 레시피 상세 조회
//    @GetMapping("/view")
//    public String showRecipeDetail(@RequestParam String recipeId, Model model) {
//        // ✅ 상세 들어올 때 조회수 +1
//        recipeService.viewCount(recipeId);
//
//        RecipeDto recipe = recipeService.getRecipeDetail(recipeId);
//        model.addAttribute("recipe", recipe);
//        return "recipe/recipeview";  // ✅ /WEB-INF/jsp/recipe/recipeview.jsp
//    }
//    // ✅ 4. 레시피 JSON API (Flask에서 호출용)
//    @GetMapping("/api/list")
//    @ResponseBody
//    public List<RecipeDto> getRecipeListApi(
//            @RequestParam(defaultValue = "") String categoryKr,
//            @RequestParam(required = false) String dustGood // ✅ 추가
//    ) {
//        if (dustGood != null && dustGood.equalsIgnoreCase("Y")) {
//            // ✅ dust_good 전용 조회
//            return recipeService.getRandomDustGood(50); // 최대 50개 가져오기
//        }
//
//        // ✅ 기존 카테고리 조회 그대로 유지
//        return recipeService.getRecipeList(categoryKr, "", PageRequest.of(0, 50)).getContent();
//    }
//    // ✅ 5. 미세먼지에 좋은 음식 JSON API
//    @GetMapping("/api/dust-good")
//    @ResponseBody
//    public List<RecipeDto> getDustGoodRecipes(
//            @RequestParam(defaultValue = "5") int count // 기본 5개 가져오기
//    ) {
//        return recipeService.getRandomDustGood(count);
//    }
//}

package com.simplecoding.cheforest.jpa.recipe.controller;

import com.simplecoding.cheforest.jpa.recipe.dto.RecipeDto;
import com.simplecoding.cheforest.jpa.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    // ✅ 1. 이미지 캐싱 실행 (관리자 전용)
    @PostMapping("/cache-images")
    @ResponseBody
    public String cacheImages() {
        recipeService.downloadAndCacheAllImages();
        return "✅ 이미지 캐싱 완료!";
    }

    // ✅ 2. 레시피 목록 조회 (카테고리 + 검색 + 검색타입 + 페이징 + 전체총합)
    @GetMapping("/list")
    public String showRecipeList(
            @RequestParam(defaultValue = "") String categoryKr,
            @RequestParam(defaultValue = "") String searchKeyword,
            @RequestParam(defaultValue = "title") String searchType, // 👈 [추가] 검색 타입 (title 또는 ingredient)
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);

        // (필터 반영) 현재 페이지 데이터
        // 🚨 [수정] searchType 인자를 추가하여 Service에 전달
        Page<RecipeDto> recipePage = recipeService.getRecipeList(categoryKr, searchKeyword, searchType, pageable);

        // 카테고리별 개수 맵  {"한식":77, "양식":87, ...}
        Map<String, Long> recipeCountMap = recipeService.getCategoryCounts();

        // ✅ 전체 총합(필터와 무관, ‘전체’ 뱃지에 쓰는 값)
        long allTotalCount = recipeService.countAllRecipes();

        // (선택) 사이드바 노출 순서 고정(추후 수정)
        List<String> categoryOrder = java.util.Arrays.asList("한식","양식","중식","일식","디저트");

        model.addAttribute("recipeList", recipePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", recipePage.getTotalPages());
        model.addAttribute("categoryKr", categoryKr);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("searchType", searchType); // 👈 [추가] 뷰(JSP)에서 현재 검색 타입을 유지하기 위해 모델에 추가

        // 상단 “전체 레시피 n개”는 현재 필터/검색 기준
        model.addAttribute("totalCount", recipePage.getTotalElements());

        // ✅ 새로 추가
        model.addAttribute("allTotalCount", allTotalCount);
        model.addAttribute("recipeCountMap", recipeCountMap);
        model.addAttribute("categoryOrder", categoryOrder);

        // 기존
        model.addAttribute("best3Recipes", recipeService.getBest3Recipes());
        model.addAttribute("categoryList", recipeService.getAllCategories());

        return "recipe/recipelist";
    }

    // ✅ 3. 레시피 상세 조회
    @GetMapping("/view")
    public String showRecipeDetail(@RequestParam String recipeId, Model model) {
        // ✅ 상세 들어올 때 조회수 +1
        recipeService.viewCount(recipeId);

        RecipeDto recipe = recipeService.getRecipeDetail(recipeId);
        model.addAttribute("recipe", recipe);
        return "recipe/recipeview";  // ✅ /WEB-INF/jsp/recipe/recipeview.jsp
    }
    // ✅ 4. 레시피 JSON API (Flask에서 호출용)
    @GetMapping("/api/list")
    @ResponseBody
    public List<RecipeDto> getRecipeListApi(
            @RequestParam(defaultValue = "") String categoryKr,
            @RequestParam(required = false) String dustGood // ✅ 추가
    ) {
        if (dustGood != null && dustGood.equalsIgnoreCase("Y")) {
            // ✅ dust_good 전용 조회
            return recipeService.getRandomDustGood(50); // 최대 50개 가져오기
        }

        // ✅ 기존 카테고리 조회 그대로 유지
        // 🚨 [수정] searchType 파라미터가 없으므로 "title"을 기본값으로 하드코딩하여 전달
        return recipeService.getRecipeList(categoryKr, "", "title", PageRequest.of(0, 50)).getContent();
    }
    // ✅ 5. 미세먼지에 좋은 음식 JSON API
    @GetMapping("/api/dust-good")
    @ResponseBody
    public List<RecipeDto> getDustGoodRecipes(
            @RequestParam(defaultValue = "5") int count // 기본 5개 가져오기
    ) {
        return recipeService.getRandomDustGood(count);
    }
}
