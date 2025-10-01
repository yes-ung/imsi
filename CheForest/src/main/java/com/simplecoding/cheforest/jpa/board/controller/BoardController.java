package com.simplecoding.cheforest.jpa.board.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplecoding.cheforest.jpa.auth.entity.Member;
import com.simplecoding.cheforest.jpa.auth.repository.MemberRepository;
import com.simplecoding.cheforest.jpa.auth.security.CustomUserDetails;
import com.simplecoding.cheforest.jpa.board.dto.*;
import com.simplecoding.cheforest.jpa.board.service.BoardService;
import com.simplecoding.cheforest.jpa.file.dto.FileDto;
import com.simplecoding.cheforest.jpa.file.service.FileService;
import com.simplecoding.cheforest.jpa.auth.dto.MemberDetailDto;
import com.simplecoding.cheforest.jpa.mypage.service.MypageService;
import com.simplecoding.cheforest.jpa.point.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * BoardController - 목록/상세/등록/수정/삭제 + 파일URL 주입(썸네일/조리법)
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final MypageService mypageService;
    private final MemberRepository memberRepository;
    private final PointService pointService;

    /**
     * FileDto -> 브라우저에서 접근 가능한 공개 URL로 변환
     * (미리보기 inline 엔드포인트 사용)
     */
    private String toPublicUrl(FileDto f) {
        if (f == null) return null;
        return "/file/board/preview/" + f.getFileId();
    }

    // 1. 목록 조회
    @GetMapping("/board/list")
    public String list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String searchType,
            @PageableDefault(size = 9, sort = "insertTime", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails loginUser,
            Model model
    ) {
        // 로그
        log.info("👉 category='{}', keyword='{}', searchType='{}'", category, keyword, searchType);

        // 일반 게시글 목록
        Page<BoardListDto> boards = boardService.searchBoards(keyword, category, searchType, pageable);
        model.addAttribute("boards", boards.getContent());
        model.addAttribute("pageInfo", boards);

        // 인기글
        List<BoardListDto> bestPosts = (category == null || category.isBlank())
                ? boardService.getBestPosts()
                : boardService.getBestPostsByCategory(category);
        model.addAttribute("bestPosts", bestPosts);

        // 로그인 사용자 통계
        if (loginUser != null) {
            Long memberIdx = loginUser.getMember().getMemberIdx();

            long myPostsCount       = mypageService.getMyPostsCount(memberIdx, null);
            long likedPostsCount    = mypageService.getLikedBoardsCount(memberIdx, null);
            long receivedLikesCount = mypageService.getReceivedBoardLikes(memberIdx);
            long myCommentsCount    = mypageService.getMyCommentsCount(memberIdx, null);

            model.addAttribute("myPostsTotalCount", myPostsCount);
            model.addAttribute("likedPostsTotalCount", likedPostsCount);
            model.addAttribute("receivedLikesTotalCount", receivedLikesCount);
            model.addAttribute("myCommentsTotalCount", myCommentsCount);
        }

        return "board/boardlist";
    }

    // 2. 글 작성 폼
    @GetMapping("/board/add")
    public String showAddForm(Model model) {
        model.addAttribute("board", new BoardSaveReq());
        return "board/boardwrite";
    }

    // 3. 글 등록
    @PostMapping("/board/add")
    public String add(
            @ModelAttribute BoardSaveReq dto,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(value = "instructionImage", required = false) List<MultipartFile> steps, // [KEEP]
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) throws IOException {


        // 로그인
        Long memberIdx = loginUser.getMember().getMemberIdx();
        String email   = loginUser.getMember().getEmail();

        // ✅ 로그인한 회원 엔티티 조회
        Member member = memberRepository.findById(loginUser.getMember().getMemberIdx())
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        // 1) 게시글 저장 → ID 확보
        Long boardId = boardService.create(dto, email);

        // 2) 대표(썸네일) 저장
        if (thumbnail != null && !thumbnail.isEmpty()) {
            FileDto thumb = fileService.saveFile(thumbnail, "BOARD", boardId, "THUMBNAIL", memberIdx);
            if (thumb != null) {
                // ★ 썸네일을 브라우저 공개 URL로 저장해두면 이후 조회도 간편
                String publicThumbUrl = toPublicUrl(thumb);
                boardService.updateThumbnail(boardId, publicThumbUrl);
            }
        }

        // 3) 단계(조리법) 이미지 저장 (내부에서 STEP_1, STEP_2 ...로 저장되도록 구현되어 있어야 함)
        if (steps != null && !steps.isEmpty()) {
            fileService.saveBoardFiles(boardId, memberIdx, steps);
        }

        // 4) 목록으로 리다이렉트
        String encodedCategory = URLEncoder.encode(
                dto.getCategory() == null ? "" : dto.getCategory(),
                StandardCharsets.UTF_8
        );

        // 3) 포인트 적립 (글 작성 기준)
        pointService.addPointWithLimit(member, "POST");

        return "redirect:/board/list?category=" + encodedCategory;
    }

    // 4. 수정 페이지
    @GetMapping("/board/edition")
    public String editForm(@RequestParam("boardId") Long boardId, Model model) {
        BoardDetailDto board = boardService.getBoardDetail(boardId);
        model.addAttribute("board", board);

        List<FileDto> fileList = fileService.getFilesByBoardId(boardId);
        model.addAttribute("fileList", fileList);

        return "board/boardedit";
    }

    // 5. 글 수정
    @PostMapping("/board/edit")
    public String update(@ModelAttribute BoardUpdateReq dto,
                         @RequestParam(value = "images", required = false) List<MultipartFile> images,
                         @RequestParam(value = "deleteImageIds", required = false) List<Long> deleteImageIds,
                         @AuthenticationPrincipal MemberDetailDto loginUser) throws IOException {

        boardService.update(dto, loginUser.getEmail(), images, deleteImageIds);

        String encodedCategory = URLEncoder.encode(dto.getCategory(), StandardCharsets.UTF_8);
        return "redirect:/board/list?category=" + encodedCategory;
    }

    // 6. 글 삭제
    @PostMapping("/board/delete")
    public String delete(@RequestParam("boardId") Long boardId) {
        boardService.delete(boardId);
        return "redirect:/board/list";
    }

    // 7. 관리자 삭제
    @PostMapping("/board/adminDelete")
    public String adminDelete(@RequestParam("boardId") Long boardId,
                              @AuthenticationPrincipal MemberDetailDto loginUser) {
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            return "redirect:/board/list?error=unauthorized";
        }
        boardService.adminDelete(boardId);
        return "redirect:/board/list";
    }

    // 8. 상세 조회 (썸네일 + 조리법 이미지 URL 주입)
    @GetMapping("/board/view")
    public String view(@RequestParam("boardId") Long boardId, Model model,
                       @AuthenticationPrincipal MemberDetailDto loginUser) throws Exception {
        BoardDetailDto board = boardService.getBoardDetail(boardId);

        // 조리법 JSON 파싱
        ObjectMapper mapper = new ObjectMapper();
        List<StepDto> instructions = new ArrayList<>();
        if (board.getContent() != null && !board.getContent().isBlank()) {
            try {
                instructions = mapper.readValue(
                        board.getContent(),
                        new TypeReference<List<StepDto>>() {}
                );
            } catch (Exception e) {
                log.warn("조리법 JSON 파싱 실패: {}", e.getMessage());
            }
        }

        // 파일 목록 조회
        List<FileDto> files = fileService.getFilesByBoardId(boardId);

        // 썸네일 URL (THUMBNAIL 우선, 없으면 board.thumbnail 사용)
        String thumbnailPath = files.stream()
                .filter(f -> "THUMBNAIL".equalsIgnoreCase(f.getUsePosition()))
                .findFirst()
                .map(this::toPublicUrl)     // ★ 공개 URL 변환
                .orElse(board.getThumbnail());

        // 단계 이미지 URL을 instructions[i].image에 주입 (STEP_1, STEP_2, ...)
        for (int i = 0; i < instructions.size(); i++) {
            final int stepNo = i + 1;
            String stepUrl = files.stream()
                    .filter(f -> ("STEP_" + stepNo).equalsIgnoreCase(f.getUsePosition()))
                    .findFirst()
                    .map(this::toPublicUrl) // ★ 공개 URL 변환
                    .orElse(null);
            if (stepUrl != null) {
                instructions.get(i).setImage(stepUrl);
            }
        }

        model.addAttribute("board", board);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("instructions", instructions);
        model.addAttribute("thumbnailPath", thumbnailPath);

        boolean isOwner = (loginUser != null)
                && (loginUser.getNickname() != null)
                && loginUser.getNickname().equals(board.getNickname());
        model.addAttribute("isOwner", isOwner);

        return "board/boardview";
    }

    // 카테고리별 총 게시글
    @GetMapping("/board/counts")
    @ResponseBody
    public Map<String, Long> getBoardCounts() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("all", boardService.getTotalCount());
        counts.put("한식", boardService.getCountByCategory("한식"));
        counts.put("양식", boardService.getCountByCategory("양식"));
        counts.put("중식", boardService.getCountByCategory("중식"));
        counts.put("일식", boardService.getCountByCategory("일식"));
        counts.put("디저트", boardService.getCountByCategory("디저트"));
        return counts;
    }

    // ===== 추가 페이지 이동 =====
    @GetMapping("/guide")
    public String guide() {
        return "support/guide";
    }
}
