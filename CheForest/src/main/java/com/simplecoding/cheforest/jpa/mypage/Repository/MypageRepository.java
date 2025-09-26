package com.simplecoding.cheforest.jpa.mypage.Repository;

import com.simplecoding.cheforest.jpa.mypage.dto.MypageLikedBoardDto;
import com.simplecoding.cheforest.jpa.mypage.dto.MypageLikedRecipeDto;
import com.simplecoding.cheforest.jpa.mypage.dto.MypageMyPostDto;

import com.simplecoding.cheforest.jpa.auth.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MypageRepository extends JpaRepository<Member, Long> {

    // 내가 작성한 글 (Board)
    @Query("SELECT new com.simplecoding.cheforest.jpa.mypage.dto.MypageMyPostDto(" +
            "b.boardId, b.title, b.insertTime, b.viewCount, b.likeCount) " +
            "FROM Board b " +
            "WHERE b.writer.memberIdx = :memberIdx " +
            "AND (:keyword IS NULL OR b.title LIKE %:keyword%) " +
            "ORDER BY b.insertTime DESC")
    Page<MypageMyPostDto> findMyPosts(@Param("memberIdx") Long memberIdx,
                                      @Param("keyword") String keyword,
                                      Pageable pageable);

    @Query("SELECT COUNT(b) FROM Board b " +
            "WHERE b.writer.memberIdx = :memberIdx " +
            "AND (:keyword IS NULL OR b.title LIKE %:keyword%)")
    int countMyPosts(@Param("memberIdx") Long memberIdx,
                     @Param("keyword") String keyword);

    // 내가 좋아요한 게시글
    @Query("SELECT new com.simplecoding.cheforest.jpa.mypage.dto.MypageLikedBoardDto(" +
            "b.boardId, b.title, m.nickname, b.insertTime, b.viewCount, b.likeCount) " +
            "FROM Like l " +
            "JOIN Board b ON l.boardId = b.boardId " +
            "JOIN b.writer m " +
            "WHERE l.member.memberIdx = :memberIdx " +
            "AND l.likeType = 'BOARD' " +
            "AND (:keyword IS NULL OR b.title LIKE %:keyword%) " +
            "ORDER BY l.likeDate DESC")
    Page<MypageLikedBoardDto> findLikedBoards(@Param("memberIdx") Long memberIdx,
                                              @Param("keyword") String keyword,
                                              Pageable pageable);

    @Query("SELECT COUNT(l) FROM Like l " +
            "JOIN Board b ON l.boardId = b.boardId " +
            "WHERE l.member.memberIdx = :memberIdx " +
            "AND l.likeType = 'BOARD' " +
            "AND (:keyword IS NULL OR b.title LIKE %:keyword%)")
    int countLikedBoards(@Param("memberIdx") Long memberIdx,
                         @Param("keyword") String keyword);

    // 내가 좋아요한 레시피
    @Query("SELECT new com.simplecoding.cheforest.jpa.mypage.dto.MypageLikedRecipeDto(" +
            "r.recipeId, r.titleKr, r.categoryKr, r.likeCount) " +
            "FROM Like l " +
            "JOIN Recipe r ON l.recipeId = r.recipeId " +
            "WHERE l.member.memberIdx = :memberIdx " +
            "AND l.likeType = 'RECIPE' " +
            "AND (:keyword IS NULL OR r.titleKr LIKE %:keyword%) " +
            "ORDER BY l.likeDate DESC")
    Page<MypageLikedRecipeDto> findLikedRecipes(@Param("memberIdx") Long memberIdx,
                                                @Param("keyword") String keyword,
                                                Pageable pageable);

    @Query("SELECT COUNT(l) FROM Like l " +
            "JOIN Recipe r ON l.recipeId = r.recipeId " +
            "WHERE l.member.memberIdx = :memberIdx " +
            "AND l.likeType = 'RECIPE' " +
            "AND (:keyword IS NULL OR r.titleKr LIKE %:keyword%)")
    int countLikedRecipes(@Param("memberIdx") Long memberIdx,
                          @Param("keyword") String keyword);

    // 내가 받은 좋아요 (내가 작성한 게시글 기준)
    @Query("SELECT COUNT(l) " +
            "FROM Like l " +
            "JOIN Board b ON l.boardId = b.boardId " +
            "WHERE b.writer.memberIdx = :memberIdx " +
            "AND l.likeType = 'BOARD'")
    int countReceivedBoardLikes(@Param("memberIdx") Long memberIdx);

    // 내가 작성한 댓글 수
    @Query("SELECT COUNT(r) FROM Review r " +
            "WHERE r.writer.memberIdx = :memberIdx " +
            "AND (:keyword IS NULL OR r.content LIKE %:keyword%)")
    int countMyComments(@Param("memberIdx") Long memberIdx,
                        @Param("keyword") String keyword);
}
