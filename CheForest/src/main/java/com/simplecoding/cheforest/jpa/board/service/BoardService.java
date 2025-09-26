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

    // 1. 목록 조회 (검색 + 페이징)
    @Transactional(readOnly = true)
    public Page<BoardListDto> searchBoards(String keyword, String category, Pageable pageable) {

        Page<BoardListDto> result = boardRepositoryDsl.searchBoards(keyword, category, pageable);

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
        // ✅ DSL 사용
        BoardDetailDto dto = boardRepositoryDsl.findBoardDetail(boardId);
        if (dto == null) {
            throw new IllegalArgumentException("게시글 없음: " + boardId);
        }

        dto.setInsertTimeStr(com.simplecoding.cheforest.common.util.DateTimeUtil.format(dto.getInsertTime()));

//         조회수 증가 (DB 반영)
        boardRepository.increaseViewCount(boardId);
        dto.setViewCount(dto.getViewCount() + 1);

        return dto;
    }

    // 3. 게시글 등록
    @Transactional
    public Long create(BoardSaveReq dto, String writerEmail) throws IOException {
        // 1. 작성자 조회
        Member writer = memberRepository.findByEmail(writerEmail)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + writerEmail));

        // 2. Board 엔티티 생성
        Board board = mapStruct.toEntity(dto);
        board.setWriter(writer);

        // 2-1. 재료/계량 리스트를 문자열로 변환 (CSV 저장)
        board.setPrepare(StringUtil.joinList(dto.getIngredientName()));     // 재료명
        board.setPrepareAmount(StringUtil.joinList(dto.getIngredientAmount())); // 계량

        // 2-2. 조리법(JSON 변환 후 content 에 저장)
        List<StepDto> steps = new ArrayList<>();
        if (dto.getInstructionContent() != null) {
            for (int i = 0; i < dto.getInstructionContent().size(); i++) {
                String text = dto.getInstructionContent().get(i);
                String image = null;
                if (dto.getInstructionImage() != null &&
                        dto.getInstructionImage().size() > i &&
                        !dto.getInstructionImage().get(i).isEmpty()) {
                    // 조리법 이미지 저장
                    FileDto file = fileService.saveFile(dto.getInstructionImage().get(i),
                            "BOARD", null, "INSTRUCTION", writer.getMemberIdx());
                    image = file != null ? file.getFilePath() : null;
                }
                steps.add(new StepDto(text, image));
            }
        }
        board.setContent(JsonUtil.toJson(steps));

        // 3. Board 저장 (일단 저장해야 boardId 생성됨)
        boardRepository.save(board);
        Long boardId = board.getBoardId();

        // 4. 대표 이미지 저장
        if (dto.getMainImage() != null && !dto.getMainImage().isEmpty()) {
            FileDto thumbnail = fileService.saveFile(dto.getMainImage(),
                    "BOARD", boardId, "THUMBNAIL", writer.getMemberIdx());
            if (thumbnail != null) {
                board.setThumbnail(thumbnail.getFilePath());
            }
        }

        // 5. 조리법 이미지 (이미 위에서 StepDto 처리할 때 저장했으므로 별도 필요 X)
        // → 만약 추가 관리가 필요하다면 fileService.saveBoardFiles 사용 가능

        return boardId;
    }

    // 4. 게시글 수정
//    @Transactional
//    public void update(Long boardId, BoardUpdateReq dto, String writerEmail) throws IOException {
//        // 1) 기존 게시글 찾기
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + boardId));
//
//        // 작성자 검증 (선택 사항)
//        if (!board.getWriter().getEmail().equals(writerEmail)) {
//            throw new SecurityException("작성자만 수정할 수 있습니다.");
//        }
//
//        // 2) 텍스트/기본 정보 업데이트
//        // MapStruct: null 값은 무시하고 채워진 값만 업데이트
//        mapStruct.updateEntity(dto, board);
//
//        // 3) 대표 이미지 수정 (새 파일 있을 때만 교체)
//        if (dto.getMainImage() != null && !dto.getMainImage().isEmpty()) {
//            FileDto thumbnail = fileService.saveFile(dto.getMainImage(),
//                    "BOARD", boardId, "THUMBNAIL", board.getWriter().getMemberIdx());
//            if (thumbnail != null) {
//                board.setThumbnail(thumbnail.getFilePath());
//            }
//        }
//
//        // 4) 조리법 이미지 수정 (옵션)
//        if (dto.getInstructionImage() != null && !dto.getInstructionImage().isEmpty()) {
//            fileService.saveBoardFiles(boardId, board.getWriter().getMemberIdx(), dto.getInstructionImage());
//            // 기존 이미지를 지우고 싶다면 여기서 삭제 로직 추가 가능
//        }
//    }

    // 5. 게시글 삭제
    public void delete(Long boardId) {
        likeService.deleteAllByBoardId(boardId);
        fileService.deleteAllByTargetIdAndType(boardId, "board");
        boardRepository.deleteById(boardId);
    }

    // 6. 관리자 삭제
    public void adminDelete(Long boardId) {
        likeService.deleteAllByBoardId(boardId);
        fileService.deleteAllByTargetIdAndType(boardId, "board");
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
}
