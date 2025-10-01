package com.simplecoding.cheforest.jpa.like.service;

import com.simplecoding.cheforest.jpa.board.entity.Board;
import com.simplecoding.cheforest.jpa.board.repository.BoardRepository;
import com.simplecoding.cheforest.jpa.like.dto.LikeRes;
import com.simplecoding.cheforest.jpa.like.dto.LikeSaveReq;
import com.simplecoding.cheforest.jpa.like.entity.Like;
import com.simplecoding.cheforest.jpa.like.repository.LikeRepository;
import com.simplecoding.cheforest.jpa.auth.entity.Member;
import com.simplecoding.cheforest.jpa.auth.repository.MemberRepository;
import com.simplecoding.cheforest.jpa.recipe.entity.Recipe;
import com.simplecoding.cheforest.jpa.recipe.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final RecipeRepository recipeRepository;

    // 좋아요 여부 확인 (게시판)
    public boolean existsBoardLike(Long memberIdx, Long boardId) {
        Member member = memberRepository.findById(memberIdx).orElseThrow();
        return likeRepository.existsByMemberAndBoardId(member, boardId);
    }

    // 좋아요 여부 확인 (레시피)
    public boolean existsRecipeLike(Long memberIdx, String recipeId) {
        Member member = memberRepository.findById(memberIdx).orElseThrow();
        return likeRepository.existsByMemberAndRecipeId(member, recipeId);
    }

    // 좋아요 추가
    public LikeRes addLike(LikeSaveReq req) {
        Member member = memberRepository.findById(req.getMemberIdx()).orElseThrow();

        if ("BOARD".equals(req.getLikeType())) {
            Board board = boardRepository.findById(req.getBoardId()).orElseThrow();

            Like like = Like.builder()
                    .member(member)
                    .boardId(board.getBoardId())
                    .likeType("BOARD")
                    .build();
            likeRepository.save(like);
            likeRepository.increaseBoardLikeCount(board.getBoardId());

            Long latestCount = boardRepository.findById(board.getBoardId())
                    .orElseThrow().getLikeCount();

            return LikeRes.builder()
                    .likeId(like.getLikeId())
                    .likeType("BOARD")
                    .boardId(board.getBoardId())
                    .likeCount(latestCount)
                    .build();

        } else {
            Recipe recipe = recipeRepository.findById(req.getRecipeId()).orElseThrow();

            Like like = Like.builder()
                    .member(member)
                    .recipeId(recipe.getRecipeId())
                    .likeType("RECIPE")
                    .build();
            likeRepository.save(like);
            likeRepository.increaseRecipeLikeCount(recipe.getRecipeId());
            // 최신 카운트 조회
            Long latestCount = recipeRepository.findById(recipe.getRecipeId())
                    .orElseThrow().getLikeCount();

            return LikeRes.builder()
                    .likeId(like.getLikeId())
                    .likeType("RECIPE")
                    .recipeId(recipe.getRecipeId())
                    .likeCount(latestCount)
                    .build();
        }
    }


    // 좋아요 취소
    public LikeRes removeLike(LikeSaveReq req) {
        Member member = memberRepository.findById(req.getMemberIdx()).orElseThrow();

        if ("BOARD".equals(req.getLikeType())) {
            likeRepository.deleteByMemberAndBoardId(member, req.getBoardId());
            likeRepository.decreaseBoardLikeCount(req.getBoardId());

            Long latestCount = boardRepository.findById(req.getBoardId())
                    .orElseThrow().getLikeCount();

            return LikeRes.builder()
                    .likeType("BOARD")
                    .boardId(req.getBoardId())
                    .likeCount(latestCount)
                    .build();

        } else {
            likeRepository.deleteByMemberAndRecipeId(member, req.getRecipeId());
            likeRepository.decreaseRecipeLikeCount(req.getRecipeId());

            Long latestCount = recipeRepository.findById(req.getRecipeId())
                    .orElseThrow().getLikeCount();

            return LikeRes.builder()
                    .likeType("RECIPE")
                    .recipeId(req.getRecipeId())
                    .likeCount(latestCount)
                    .build();
        }
    }

    // 좋아요 수 조회
    public long countBoardLikes(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow().getLikeCount();
    }

    public long countRecipeLikes(String recipeId) {
        return recipeRepository.findById(recipeId).orElseThrow().getLikeCount();
    }

    // 전체 삭제 (게시판/레시피/회원)
    public void deleteAllByBoardId(Long boardId) {
        likeRepository.deleteAllByBoardId(boardId);
    }

    public void deleteAllByRecipeId(String recipeId) {
        likeRepository.deleteAllByRecipeId(recipeId);
    }

    public void deleteAllByMember(Long memberIdx) {
        Member member = memberRepository.findById(memberIdx).orElseThrow();
        likeRepository.deleteAllByMember(member);
    }
}
