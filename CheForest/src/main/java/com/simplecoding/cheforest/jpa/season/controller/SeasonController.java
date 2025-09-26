package com.simplecoding.cheforest.jpa.season.controller;

import com.simplecoding.cheforest.jpa.season.entity.SeasonIngredient;
import com.simplecoding.cheforest.jpa.season.repository.SeasonIngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SeasonController {

    private final SeasonIngredientRepository seasonIngredientRepository;

    @GetMapping("/season")
    public String showSeason(Model model) {
        List<SeasonIngredient> spring = seasonIngredientRepository.findBySeasons("spring");
        List<SeasonIngredient> summer = seasonIngredientRepository.findBySeasons("summer");
        List<SeasonIngredient> autumn = seasonIngredientRepository.findBySeasons("autumn");
        List<SeasonIngredient> winter = seasonIngredientRepository.findBySeasons("winter");

        // ✅ 순서 랜덤 섞기
        Collections.shuffle(spring);
        Collections.shuffle(summer);
        Collections.shuffle(autumn);
        Collections.shuffle(winter);

        model.addAttribute("springIngredients", spring);
        model.addAttribute("summerIngredients", summer);
        model.addAttribute("autumnIngredients", autumn);
        model.addAttribute("winterIngredients", winter);

        // 기본 탭 표시용
        model.addAttribute("season", "spring");

        log.info("loaded counts -> spring={}, summer={}, autumn={}, winter={}",
                spring.size(), summer.size(), autumn.size(), winter.size());

        return "season/seasonIngredient";
    }
}
