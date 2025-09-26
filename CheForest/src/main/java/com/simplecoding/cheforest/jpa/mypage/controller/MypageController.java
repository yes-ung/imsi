package com.simplecoding.cheforest.jpa.mypage.controller;

import com.simplecoding.cheforest.jpa.auth.entity.Member;
import com.simplecoding.cheforest.jpa.auth.repository.MemberRepository;
import com.simplecoding.cheforest.jpa.mypage.dto.MypageLikedBoardDto;
import com.simplecoding.cheforest.jpa.mypage.dto.MypageLikedRecipeDto;
import com.simplecoding.cheforest.jpa.mypage.dto.MypageMyPostDto;

import com.simplecoding.cheforest.jpa.mypage.service.MypageService;
import com.simplecoding.cheforest.jpa.auth.security.CustomUserDetails;
import com.simplecoding.cheforest.jpa.point.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;
    private final PointService pointService;
    private final MemberRepository memberRepository;

    // 마이페이지 메인 (내 글 + 좋아요)
    @GetMapping("/mypage")
    public String myPage(
            @RequestParam(value = "tab", defaultValue = "myboard") String tab,
            @RequestParam(value = "myPostsPage", defaultValue = "1") int myPostsPage,
            @RequestParam(value = "likedPostsPage", defaultValue = "1") int likedPostsPage,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            @AuthenticationPrincipal CustomUserDetails loginUser,   // ✅ Spring Security 인증 객체 주입
            Model model) {

        if (loginUser == null || loginUser.getMember().getMemberIdx() == null) {
            return "redirect:/auth/login"; // 로그인 안된 경우
        }

        Long memberIdx = loginUser.getMember().getMemberIdx();

        // 내가 작성한 글 (페이징)
        Pageable myPostsPageable = PageRequest.of(myPostsPage - 1, 10, Sort.by("insertTime").descending());
        Page<MypageMyPostDto> myPosts = mypageService.getMyPosts(memberIdx, searchKeyword, myPostsPageable);
        model.addAttribute("myPosts", myPosts.getContent());
        model.addAttribute("myPostsTotalCount", myPosts.getTotalElements());
        model.addAttribute("myPostsPaginationInfo", myPosts);

        // 내가 좋아요한 레시피 (페이징)
        Pageable likedRecipesPageable = PageRequest.of(likedPostsPage - 1, 10, Sort.by("likeDate").descending());
        Page<MypageLikedRecipeDto> likedRecipes = mypageService.getLikedRecipes(memberIdx, searchKeyword, likedRecipesPageable);
        model.addAttribute("likedRecipes", likedRecipes.getContent());
        model.addAttribute("likedRecipesTotalCount", likedRecipes.getTotalElements());
        model.addAttribute("likedRecipesPaginationInfo", likedRecipes);

        // 내가 좋아요한 게시글 (페이징)
        Pageable likedBoardsPageable = PageRequest.of(likedPostsPage - 1, 10, Sort.by("likeDate").descending());
        Page<MypageLikedBoardDto> likedBoards = mypageService.getLikedBoards(memberIdx, searchKeyword, likedBoardsPageable);
        model.addAttribute("likedPosts", likedBoards.getContent());
        model.addAttribute("likedPostsTotalCount", likedBoards.getTotalElements());
        model.addAttribute("likedPostsPaginationInfo", likedBoards);

        // 현재 탭 상태
        model.addAttribute("tab", tab);

        return "mypage/mypage";
    }

    @GetMapping("/points")
    public String myPoints(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        Member member = memberRepository.findById(user.getMemberIdx())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        model.addAttribute("currentPoint", member.getPoint());
        model.addAttribute("currentGrade", member.getGrade());
        model.addAttribute("todayPoint", pointService.getTodayPoints(member.getMemberIdx()));
        model.addAttribute("weekPoint", pointService.getWeekPoints(member.getMemberIdx()));
        model.addAttribute("nextGradePoint", pointService.getNextGradePoint(member.getPoint()));
        model.addAttribute("recentHistories", pointService.getRecentHistories(member.getMemberIdx()));

        return "mypage/points";
    }
}
