package com.simplecoding.cheforest.jpa.like.controller;

import com.simplecoding.cheforest.jpa.like.dto.LikeRes;
import com.simplecoding.cheforest.jpa.like.dto.LikeSaveReq;
import com.simplecoding.cheforest.jpa.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /** 👍 좋아요 등록 */
    @PostMapping("/add")
    public LikeRes addLike(@RequestBody LikeSaveReq req) {
        log.info("📥 addLike 요청: {}", req);

        if ("BOARD".equalsIgnoreCase(req.getLikeType())) {
            if (likeService.existsBoardLike(req.getMemberIdx(), req.getBoardId())) {
                log.info("⚠️ 이미 좋아요 누름");
                return LikeRes.builder()
                        .likeType("BOARD")
                        .boardId(req.getBoardId())
                        .likeCount(likeService.countBoardLikes(req.getBoardId()))
                        .build();
            }
        } else if ("RECIPE".equalsIgnoreCase(req.getLikeType())) {
            if (likeService.existsRecipeLike(req.getMemberIdx(), req.getRecipeId())) {
                log.info("⚠️ 이미 좋아요 누름");
                return LikeRes.builder()
                        .likeType("RECIPE")
                        .recipeId(req.getRecipeId())
                        .likeCount(likeService.countRecipeLikes(req.getRecipeId()))
                        .build();
            }
        }

        return likeService.addLike(req);
    }

    /** ❌ 좋아요 취소 */
    @PostMapping("/remove")
    public LikeRes removeLike(@RequestBody LikeSaveReq req) {
        log.info("📥 removeLike 요청: {}", req);

        if ("BOARD".equalsIgnoreCase(req.getLikeType())) {
            if (!likeService.existsBoardLike(req.getMemberIdx(), req.getBoardId())) {
                log.info("⚠️ 취소 요청했지만 좋아요 안 되어 있음");
                return LikeRes.builder()
                        .likeType("BOARD")
                        .boardId(req.getBoardId())
                        .likeCount(likeService.countBoardLikes(req.getBoardId()))
                        .build();
            }
        } else if ("RECIPE".equalsIgnoreCase(req.getLikeType())) {
            if (!likeService.existsRecipeLike(req.getMemberIdx(), req.getRecipeId())) {
                log.info("⚠️ 취소 요청했지만 좋아요 안 되어 있음");
                return LikeRes.builder()
                        .likeType("RECIPE")
                        .recipeId(req.getRecipeId())
                        .likeCount(likeService.countRecipeLikes(req.getRecipeId()))
                        .build();
            }
        }

        return likeService.removeLike(req);
    }

    /** 📊 좋아요 수 조회 */
    @GetMapping("/count")
    public long getLikeCount(@RequestParam(required = false) Long boardId,
                             @RequestParam(required = false) String recipeId,
                             @RequestParam String likeType) {
        if ("BOARD".equalsIgnoreCase(likeType) && boardId != null) {
            return likeService.countBoardLikes(boardId);
        } else if ("RECIPE".equalsIgnoreCase(likeType) && recipeId != null) {
            return likeService.countRecipeLikes(recipeId);
        }
        return 0L;
    }

    /** 🔍 좋아요 여부 확인 */
    @GetMapping("/check")
    public boolean checkLike(@RequestParam Long memberIdx,
                             @RequestParam String likeType,
                             @RequestParam(required = false) Long boardId,
                             @RequestParam(required = false) String recipeId) {
        if ("BOARD".equalsIgnoreCase(likeType) && boardId != null) {
            return likeService.existsBoardLike(memberIdx, boardId);
        } else if ("RECIPE".equalsIgnoreCase(likeType) && recipeId != null) {
            return likeService.existsRecipeLike(memberIdx, recipeId);
        }
        return false;
    }
}
