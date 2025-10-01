package com.simplecoding.cheforest.jpa.mypage.controller;

import com.simplecoding.cheforest.jpa.auth.security.CustomUserDetails;
import com.simplecoding.cheforest.jpa.mypage.dto.MypageLikedBoardDto;
import com.simplecoding.cheforest.jpa.mypage.dto.MypageLikedRecipeDto;
import com.simplecoding.cheforest.jpa.mypage.dto.MypageMyPostDto;
import com.simplecoding.cheforest.jpa.mypage.service.MypageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("")
    public String mypageMain(@RequestParam(defaultValue = "myboard") String tab,
                             @RequestParam(value = "myPostsPage",    defaultValue = "1") int myPostsPage,
                             @RequestParam(value = "likedPostsPage", defaultValue = "1") int likedPostsPage,
                             @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                             @AuthenticationPrincipal CustomUserDetails loginUser,
                             Model model) {

        if (loginUser == null || loginUser.getMember() == null) {
            return "redirect:/login?redirect=/mypage";
        }
        Long memberIdx = loginUser.getMember().getMemberIdx();
        model.addAttribute("activeTab", tab);

        // ===== 상단 통계 (long 반환, 삼항 없음) =====
        long receivedLikesTotal   = mypageService.getReceivedBoardLikes(memberIdx);
        long myPostsTotalViewCount= mypageService.getMyPostsTotalViewCount(memberIdx);
        long myCommentsTotalCount = mypageService.getMyCommentsTotalCount(memberIdx);

        model.addAttribute("receivedLikesTotalCount", receivedLikesTotal);
        model.addAttribute("myPostsTotalViewCount",   myPostsTotalViewCount);
        model.addAttribute("myCommentsTotalCount",    myCommentsTotalCount);

        // ===== 내가 작성한 글 (★ 현재 빠져있던 부분 복구) =====
        Pageable myPostsPageable = PageRequest.of(myPostsPage - 1, 10, Sort.by("insertTime").descending());
        Page<MypageMyPostDto> myPosts = mypageService.getMyPosts(memberIdx, searchKeyword, myPostsPageable);
        model.addAttribute("myPosts", myPosts.getContent());
        model.addAttribute("myPostsTotalCount", myPosts.getTotalElements());
        model.addAttribute("myPostsPaginationInfo", myPosts);

        // ===== 내가 좋아요한 레시피 =====
        Pageable likedRecipesPageable = PageRequest.of(likedPostsPage - 1, 10, Sort.by("likeDate").descending());
        Page<MypageLikedRecipeDto> likedRecipes = mypageService.getLikedRecipes(memberIdx, searchKeyword, likedRecipesPageable);
        model.addAttribute("likedRecipes", likedRecipes.getContent());
        model.addAttribute("likedRecipesTotalCount", likedRecipes.getTotalElements());
        model.addAttribute("likedRecipesPaginationInfo", likedRecipes);

        // ===== 내가 좋아요한 게시글 =====
        Pageable likedBoardsPageable = PageRequest.of(likedPostsPage - 1, 10, Sort.by("likeDate").descending());
        Page<MypageLikedBoardDto> likedBoards = mypageService.getLikedBoards(memberIdx, searchKeyword, likedBoardsPageable);
        model.addAttribute("likedPosts", likedBoards.getContent());
        model.addAttribute("likedPostsTotalCount", likedBoards.getTotalElements());
        model.addAttribute("likedPostsPaginationInfo", likedBoards);

        return "mypage/mypage";
    }
}
