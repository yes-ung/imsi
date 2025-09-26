package com.simplecoding.cheforest.jpa.board.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplecoding.cheforest.jpa.auth.security.CustomUserDetails;
import com.simplecoding.cheforest.jpa.board.dto.*;
import com.simplecoding.cheforest.jpa.board.service.BoardService;
import com.simplecoding.cheforest.jpa.file.dto.FileDto;
import com.simplecoding.cheforest.jpa.file.service.FileService;
import com.simplecoding.cheforest.jpa.auth.dto.MemberDetailDto;
import com.simplecoding.cheforest.jpa.mypage.service.MypageService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final MypageService mypageService;

    // 1. 목록 조회
    @GetMapping("/board/list")
    public String list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 9) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails loginUser,
            Model model
    ) {
        // 👉 현재 카테고리/검색 로그
        log.info("👉 category = '{}'", category);

        // ✅ 일반 게시글 목록
        Page<BoardListDto> boards = boardService.searchBoards(keyword, category, pageable);
        model.addAttribute("boards", boards.getContent());
        model.addAttribute("pageInfo", boards);

        // ✅ 인기글
        List<BoardListDto> bestPosts = (category == null || category.isBlank())
                ? boardService.getBestPosts()
                : boardService.getBestPostsByCategory(category);
        model.addAttribute("bestPosts", bestPosts);

        // ✅ 로그인 사용자 관련 통계
        if (loginUser != null) {
            Long memberIdx = loginUser.getMember().getMemberIdx();

            long myPostsCount = mypageService.getMyPostsCount(memberIdx, null);
            long likedPostsCount = mypageService.getLikedBoardsCount(memberIdx, null);
            long receivedLikesCount = mypageService.getReceivedBoardLikes(memberIdx);
            long myCommentsCount = mypageService.getMyCommentsCount(memberIdx, null);

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
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) throws IOException {

        // ✅ 이메일 꺼내는 방법
        String email = loginUser.getMember().getEmail();
        Long memberIdx = loginUser.getMember().getMemberIdx();

        // 1) 글 + 대표 이미지 저장
        Long boardId = boardService.create(dto, loginUser.getEmail());

        // 2) 단계별 이미지 저장 (있으면)
        if (dto.getInstructionImage() != null && !dto.getInstructionImage().isEmpty()) {
            fileService.saveBoardFiles(boardId, loginUser.getMember().getMemberIdx(), dto.getInstructionImage());
        }

        String encodedCategory = URLEncoder.encode(dto.getCategory(), "UTF-8");
        return "redirect:/board/list?category=" + encodedCategory;
    }

    // 4. 수정 페이지
    @GetMapping("/board/edition")
    public String editForm(@RequestParam("boardId") Long boardId, Model model) {
        BoardDetailDto board = boardService.getBoardDetail(boardId);
        model.addAttribute("board", board);

        List<FileDto> fileList = fileService.getFilesByBoardId(boardId);
        model.addAttribute("fileList", fileList);

        return "board/boardupdate";
    }

    // 5. 글 수정
//    @PostMapping("/board/edit")
//    public String update(
//            @ModelAttribute BoardUpdateReq dto,
//            @RequestParam(value = "images", required = false) List<MultipartFile> images,
//            @RequestParam(value = "deleteImageIds", required = false) List<Long> deleteImageIds,
//            @AuthenticationPrincipal MemberDetailDto loginUser
//    ) throws IOException {
//        // 삭제할 파일 처리
//        if (deleteImageIds != null) {
//            deleteImageIds.forEach(fileService::deleteFile);
//        }
//
//        // 새 파일 업로드
//        Long firstFileId = fileService.saveBoardFiles(dto.getBoardId(), loginUser.getMemberIdx(), images);
//        if (firstFileId != null) {
//            boardService.updateThumbnail(dto.getBoardId(), "/file/download?fileId=" + firstFileId);
//        }
//
//        // DB 업데이트
//        boardService.update(dto, loginUser.getEmail());
//
//        String encodedCategory = URLEncoder.encode(dto.getCategory(), "UTF-8");
//        return "redirect:/board/list?category=" + encodedCategory;
//    }

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

    // 8. 상세 조회
    @GetMapping("/board/view")
    public String view(@RequestParam("boardId") Long boardId, Model model,
                       @AuthenticationPrincipal MemberDetailDto loginUser) throws Exception {
        BoardDetailDto board = boardService.getBoardDetail(boardId);

        // JSON 파싱
        ObjectMapper mapper = new ObjectMapper();
        List<StepDto> instructions = new ArrayList<>();

        if (board.getContent() != null && !board.getContent().isBlank()) {
            try {
                instructions = mapper.readValue(
                        board.getContent(),
                        new TypeReference<List<StepDto>>() {}
                );
            } catch (Exception e) {
                // 만약 JSON 파싱 실패하면 그냥 빈 리스트 유지
                e.printStackTrace();
            }
        }

        model.addAttribute("board", board);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("instructions", instructions); // JSP에서 사용할 데이터


        return "board/boardview";
    }

//    카테고리별 총 게시글 보이게하는 api
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

    @GetMapping("/qna")
    public String qna() {
        return "support/qna";
    }
}