package com.simplecoding.cheforest.jpa.home.controller;

import com.simplecoding.cheforest.jpa.auth.entity.Member;
import com.simplecoding.cheforest.jpa.auth.repository.MemberRepository;
import com.simplecoding.cheforest.jpa.auth.security.CustomUserDetails;
import com.simplecoding.cheforest.jpa.home.service.HomeService;
import com.simplecoding.cheforest.jpa.point.service.RankingService;
import com.simplecoding.cheforest.jpa.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final RecipeService recipeService;
    private final RankingService rankingService;
    private final MemberRepository memberRepository;
    private final HomeService homeService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails user, Model model) {

        model.addAttribute("popularRecipes", homeService.getPopularRecipes());
        model.addAttribute("categoryRecipes", homeService.getCategoryTop3Recipes());
        model.addAttribute("categoryBoards", homeService.getCategoryLatestBoards());

        // 랭킹
        List<Member> topMembers = rankingService.getTopRanking(10);
        model.addAttribute("topMembers", topMembers);

        if (user != null) {
            Member me = memberRepository.findById(user.getMemberIdx())
                    .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
            Long myRank = rankingService.getMyRank(me);
            model.addAttribute("myRank", myRank);
        }
        return "home"; // home.jsp
    }

    @GetMapping("/search/all")
    public String search(Model model) {
        return "search/searchAll";
    }
}
