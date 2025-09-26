package com.simplecoding.cheforest.jpa.mypage.service;

import com.simplecoding.cheforest.jpa.mypage.Repository.MypageRepository;
import com.simplecoding.cheforest.jpa.mypage.dto.MypageLikedBoardDto;
import com.simplecoding.cheforest.jpa.mypage.dto.MypageLikedRecipeDto;
import com.simplecoding.cheforest.jpa.mypage.dto.MypageMyPostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MypageService {

    private final MypageRepository mypageRepository;

    // 내가 작성한 글
    public Page<MypageMyPostDto> getMyPosts(Long memberIdx, String keyword, Pageable pageable) {
        log.info("내가 작성한 글 조회 - memberIdx={}, keyword={}", memberIdx, keyword);
        return mypageRepository.findMyPosts(memberIdx, keyword, pageable);
    }

    public long getMyPostsCount(Long memberIdx, String keyword) {
        return mypageRepository.countMyPosts(memberIdx, keyword);
    }

    // 내가 좋아요한 게시글
    public Page<MypageLikedBoardDto> getLikedBoards(Long memberIdx, String keyword, Pageable pageable) {
        return mypageRepository.findLikedBoards(memberIdx, keyword, pageable);
    }

    public long getLikedBoardsCount(Long memberIdx, String keyword) {
        return mypageRepository.countLikedBoards(memberIdx, keyword);
    }

    // 내가 좋아요한 레시피
    public Page<MypageLikedRecipeDto> getLikedRecipes(Long memberIdx, String keyword, Pageable pageable) {
        return mypageRepository.findLikedRecipes(memberIdx, keyword, pageable);
    }

    public long getLikedRecipesCount(Long memberIdx, String keyword) {
        return mypageRepository.countLikedRecipes(memberIdx, keyword);
    }

    // 내가 받은 좋아요 수
    public int getReceivedBoardLikes(Long memberIdx) {
        return mypageRepository.countReceivedBoardLikes(memberIdx);
    }

    // 내가 작성한 댓글 수
    public int getMyCommentsCount(Long memberIdx, String keyword) {
        return mypageRepository.countMyComments(memberIdx, keyword);
    }
}
