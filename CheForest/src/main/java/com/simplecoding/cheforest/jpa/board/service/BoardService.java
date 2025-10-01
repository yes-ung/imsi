package com.simplecoding.cheforest.jpa.board.service;

import com.simplecoding.cheforest.es.integratedSearch.entity.IntegratedSearch;
import com.simplecoding.cheforest.es.integratedSearch.repository.IntegratedSearchRepository;
import com.simplecoding.cheforest.jpa.board.dto.*;
import com.simplecoding.cheforest.jpa.board.entity.Board;
import com.simplecoding.cheforest.jpa.board.repository.BoardRepository;
import com.simplecoding.cheforest.jpa.board.repository.BoardRepositoryDsl;
import com.simplecoding.cheforest.jpa.common.MapStruct;
import com.simplecoding.cheforest.jpa.common.util.JsonUtil;
import com.simplecoding.cheforest.jpa.common.util.StringUtil;
import com.simplecoding.cheforest.jpa.file.dto.FileDto;
import com.simplecoding.cheforest.jpa.file.service.FileService;
import com.simplecoding.cheforest.jpa.like.service.LikeService;
import com.simplecoding.cheforest.jpa.auth.entity.Member;
import com.simplecoding.cheforest.jpa.auth.repository.MemberRepository;
import com.simplecoding.cheforest.jpa.review.service.ReviewService;   // ✅ 추가
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardRepositoryDsl boardRepositoryDsl;
    private final FileService fileService;
    private final LikeService likeService;
    private final MemberRepository memberRepository;
    private final MapStruct mapStruct;
    private final IntegratedSearchRepository integratedSearchRepository;
    private final ReviewService reviewService;   // ✅ 추가

    // 1. 목록 조회 (검색 + 페이징)
    @Transactional(readOnly = true)
    public Page<BoardListDto> searchBoards(String keyword, String category, String searchType, Pageable pageable) {
        Page<BoardListDto> result = boardRepositoryDsl.searchBoards(keyword, category, searchType, pageable);

        result.forEach(dto -> {
            if (dto.getInsertTime() != null) {
                dto.setCreatedAgo(toAgo(dto.getInsertTime()));
            }
        });
        return result;
    }

    // 2. 상세 조회 (+ 조회수 증가)
    @Transactional
    public BoardDetailDto getBoardDetail(Long boardId) {
        BoardDetailDto dto = boardRepositoryDsl.findBoardDetail(boardId);
        if (dto == null) {
            throw new IllegalArgumentException("게시글 없음: " + boardId);
        }
        dto.setInsertTimeStr(com.simplecoding.cheforest.common.util.DateTimeUtil.format(dto.getInsertTime()));

        boardRepository.increaseViewCount(boardId);
        dto.setViewCount(dto.getViewCount() + 1);

        return dto;
    }

    // 3. 게시글 등록
    @Transactional
    public Long create(BoardSaveReq dto, String writerEmail) throws IOException {
        Member writer = memberRepository.findByEmail(writerEmail)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + writerEmail));

        Board board = mapStruct.toEntity(dto);
        board.setWriter(writer);

        board.setPrepare(StringUtil.joinList(dto.getIngredientName()));
        board.setPrepareAmount(StringUtil.joinList(dto.getIngredientAmount()));

        List<StepDto> steps = new ArrayList<>();
        if (dto.getInstructionContent() != null) {
            for (int i = 0; i < dto.getInstructionContent().size(); i++) {
                String text = dto.getInstructionContent().get(i);

                steps.add(new StepDto(text, null));
            }
        }
        board.setContent(JsonUtil.toJson(steps));

        boardRepository.save(board);
        Long boardId = board.getBoardId();

        return boardId;
    }

    // 4. 게시글 수정
    @Transactional
    public void update(BoardUpdateReq dto,
                       String writerEmail,
                       List<MultipartFile> images,
                       List<Long> deleteImageIds) throws IOException {

        // 1) 게시글 찾기
        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + dto.getBoardId()));

        // 2) 작성자 검증
        if (!board.getWriter().getEmail().equals(writerEmail)) {
            throw new SecurityException("작성자만 수정할 수 있습니다.");
        }

        // 3) 텍스트 업데이트 (MapStruct: null 무시)
        mapStruct.updateEntity(dto, board);

        // 4) 기존 파일 삭제
        if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
            deleteImageIds.forEach(fileService::deleteFile);
        }

        // 5) 새 파일 저장
        Long firstNewFileId = null;
        if (images != null && !images.isEmpty()) {
            firstNewFileId = fileService.saveBoardFiles(board.getBoardId(),
                    board.getWriter().getMemberIdx(), images);
        }

        // 6) 썸네일 재지정 로직
        if (firstNewFileId != null) {
            // (A) 새 업로드가 있으면 → 첫 번째 파일을 썸네일로 교체
            board.setThumbnail("/file/board/preview/" + firstNewFileId);

        } else if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
            // (B) 새 업로드는 없는데 기존 썸네일이 삭제되었으면 → null 처리
            board.setThumbnail(null);
        }

        // 7) JPA 더티체킹 → commit 시점에 자동 반영
    }


    // 5. 게시글 삭제
    public void delete(Long boardId) {
        // ✅ 댓글 먼저 삭제
        reviewService.deleteByBoardId(boardId);

        likeService.deleteAllByBoardId(boardId);
        fileService.deleteAllByTargetIdAndType(boardId, "BOARD");
        boardRepository.deleteById(boardId);
    }

    // 6. 관리자 삭제
    public void adminDelete(Long boardId) {
        // ✅ 댓글 먼저 삭제
        reviewService.deleteByBoardId(boardId);

        likeService.deleteAllByBoardId(boardId);
        fileService.deleteAllByTargetIdAndType(boardId, "BOARD");
        boardRepository.deleteById(boardId);
    }

    // 7. 인기글 조회
    @Transactional(readOnly = true)
    public List<BoardListDto> getBestPosts() {
        List<BoardListDto> posts = boardRepositoryDsl.findBestPosts();
        posts.forEach(dto -> {
            if (dto.getInsertTime() != null) {
                dto.setCreatedAgo(toAgo(dto.getInsertTime()));
            }
        });
        return posts;
    }

    @Transactional(readOnly = true)
    public List<BoardListDto> getBestPostsByCategory(String category) {
        List<BoardListDto> posts = boardRepositoryDsl.findBestPostsByCategory(category);
        posts.forEach(dto -> {
            if (dto.getInsertTime() != null) {
                dto.setCreatedAgo(toAgo(dto.getInsertTime()));
            }
        });
        return posts;
    }

    // 8. 썸네일 업데이트
    public void updateThumbnail(Long boardId, String thumbnailPath) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + boardId));
        board.setThumbnail(thumbnailPath);
    }

    public Long countMyPosts(Member member) {
        return boardRepository.countByWriter(member);
    }

    //    총 게시글 수
    public long getTotalCount() {
        return boardRepository.count();
    }

    //    카테고리별 게시글 수
    public long getCountByCategory(String category) {
        return boardRepository.countByCategory(category);
    }


    //    전처리
    private String toAgo(java.time.LocalDateTime created) {
        java.time.Duration duration = java.time.Duration.between(created, java.time.LocalDateTime.now());

        long minutes = duration.toMinutes();
        if (minutes < 1) return "방금 전";
        if (minutes < 60) return minutes + "분 전";

        long hours = minutes / 60;
        if (hours < 24) return hours + "시간 전";

        long days = hours / 24;
        if (days < 7) return days + "일 전";

        long weeks = days / 7;
        if (weeks < 5) return weeks + "주 전";

        long months = days / 30;
        if (months < 12) return months + "개월 전";

        long years = days / 365;
        return years + "년 전";
    }
    //   최근 3개 조회
    public List<Board> recentPosts() {
        return boardRepository.findTop3ByOrderByInsertTimeDesc();
    }






}
