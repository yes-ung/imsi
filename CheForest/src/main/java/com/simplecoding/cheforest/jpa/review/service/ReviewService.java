package com.simplecoding.cheforest.jpa.review.service;

import com.simplecoding.cheforest.jpa.common.MapStruct;
import com.simplecoding.cheforest.jpa.review.dto.ReviewRes;
import com.simplecoding.cheforest.jpa.review.dto.ReviewSaveReq;
import com.simplecoding.cheforest.jpa.review.dto.ReviewUpdateReq;

import com.simplecoding.cheforest.jpa.review.entity.Review;
import com.simplecoding.cheforest.jpa.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MapStruct mapStruct;

    // 댓글 목록 조회
    public List<ReviewRes> getReviewsByBoardId(Long boardId) {
        return reviewRepository.findByBoard_BoardIdOrderByReviewIdDesc(boardId)
                .stream()
                .map(mapStruct::toDto)
                .collect(Collectors.toList());
    }

    // 댓글 등록
    public ReviewRes saveReview(ReviewSaveReq dto, Long memberIdx) {
        Review review = mapStruct.toEntity(dto);
        review.getWriter().setMemberIdx(memberIdx); // 작성자 세팅
        Review saved = reviewRepository.save(review);
        return mapStruct.toDto(saved);
    }

    // 댓글 수정
    public ReviewRes updateReview(ReviewUpdateReq dto, Long memberIdx) {
        Review review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!review.getWriter().getMemberIdx().equals(memberIdx)) {
            throw new SecurityException("본인 댓글만 수정할 수 있습니다.");
        }

        review.setContent(dto.getContent());
        return mapStruct.toDto(reviewRepository.save(review));
    }

    // 댓글 삭제
    public void deleteReview(Long reviewId, Long memberIdx) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!review.getWriter().getMemberIdx().equals(memberIdx)) {
            throw new SecurityException("본인 댓글만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
    }
}
