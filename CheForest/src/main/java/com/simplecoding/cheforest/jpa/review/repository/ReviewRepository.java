package com.simplecoding.cheforest.jpa.review.repository;

import com.simplecoding.cheforest.jpa.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 댓글 목록 (내림차순: 최신순)
    List<Review> findByBoard_BoardIdOrderByReviewIdDesc(Long boardId);

    // 게시글 삭제 시 댓글 전체 삭제
    void deleteAllByBoard_BoardId(Long boardId);

    // 회원 탈퇴 시 작성 댓글 전체 삭제 (선택적)
    void deleteAllByWriter_MemberIdx(Long memberIdx);
}
