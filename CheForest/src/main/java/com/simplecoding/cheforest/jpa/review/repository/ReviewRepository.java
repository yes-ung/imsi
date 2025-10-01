package com.simplecoding.cheforest.jpa.review.repository;

import com.simplecoding.cheforest.jpa.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 부모 댓글 (대댓글 없는 상위 댓글만)
    List<Review> findByBoard_BoardIdAndParentIdIsNullOrderByInsertTimeAsc(Long boardId);

    // 대댓글
    List<Review> findByParentIdOrderByInsertTimeAsc(Long parentId);

    // 게시글 삭제 시 댓글 전체 삭제
    void deleteByBoard_BoardId(Long boardId);
}
