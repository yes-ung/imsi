package com.simplecoding.cheforest.jpa.dust.controller;

import com.simplecoding.cheforest.jpa.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DustMapController {

    private final DustPingController dustPingController;
    private final RecipeService recipeService;
    private final RestTemplate restTemplate = new RestTemplate();

    // ✅ JSP 열기
    @GetMapping("/dustmap")
    public String dustmap() {
        return "dust/dustmap"; // /WEB-INF/views/dust/dustmap.jsp
    }

    // ✅ 지역별 미세먼지 + 레시피 추천 API
    @ResponseBody
    @GetMapping("/dust/with-recommend")
    public Map<String, Object> withRecommend(@RequestParam String sido) {
        Map<String, Object> result = new HashMap<>();

        // 1) Dust API에서 데이터 가져오기
        DustPingController.DustDto dust = dustPingController.getTodayAll()
                .stream()
                .filter(d -> d.getSido().equals(sido))
                .findFirst()
                .orElse(new DustPingController.DustDto(sido, "-", "정보없음", "-", "정보없음", "-",
                        "EX", "NoData", null, null));

        result.put("dust", dust);

        // 2) 등급에 따라 레시피 가져오기
        String grade = dust.getPm10Grade(); // "좋음", "보통", "나쁨", "매우나쁨"
        List<?> recipes;

        if ("좋음".equals(grade) || "보통".equals(grade)) {
            // 👉 Flask API 호출
            String flaskUrl = "http://localhost:5000/recommend/ai?grade=" + grade;
            try {
                ResponseEntity<List> response = restTemplate.getForEntity(flaskUrl, List.class);
                recipes = response.getBody();
            } catch (Exception e) {
                recipes = List.of(Map.of("title", "Flask API 오류", "desc", e.getMessage()));
            }
        } else {
            // 👉 DB에서 "미세먼지" 카테고리 추천
            recipes = recipeService.getRandomRecipes("미세먼지", 3);
        }

        result.put("recipes", recipes);http://localhost:8080/dustmap
        return result;
    }
}
