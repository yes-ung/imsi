package com.simplecoding.cheforest.jpa.admin.controller;


import com.simplecoding.cheforest.jpa.admin.dto.AccountStatusDTO;
import com.simplecoding.cheforest.jpa.admin.dto.CountTodayNewBoardDTO;
import com.simplecoding.cheforest.jpa.admin.dto.TodaySignUpUsersDto;
import com.simplecoding.cheforest.jpa.admin.service.AdminService;
import com.simplecoding.cheforest.jpa.auth.dto.MemberAdminDto;
import com.simplecoding.cheforest.jpa.auth.service.MemberService;
import com.simplecoding.cheforest.jpa.inquiries.dto.InquiryWithNicknameDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
// made by yes_ung
@RequiredArgsConstructor
@Log4j2
@Controller
public class AdminController {

    private final AdminService ytAdminService;
    private final MemberService MemberService;

    @GetMapping("/admin")
    public String mainTest(HttpServletRequest request, Model model) {
        request.getSession(true); // 세션이 없으면 새로 생성
//        총 가입자 수
        long allMemberCount = ytAdminService.getMemberCount();
//        금일 가입자 수
        long todayMemberCount = ytAdminService.getTodayMemberCount();
//        금일 가입자 정보
        List<TodaySignUpUsersDto>  todayMemberList = ytAdminService.getTodayMemberList();
//      활동,휴먼,제재 계정수 집계
        AccountStatusDTO accountStatusCounts = ytAdminService.getAccountStatusCounts();
//        금일 작성된 게시글,댓글 수
        CountTodayNewBoardDTO countTodayNewBoard = ytAdminService.getCountTodayNewBoard();
//        월별 작성된 게시글 및 가입자수 (통계용) json 자료형
        String monthlyActivityData = ytAdminService.getMonthlyActivityStats();


        model.addAttribute("allMemberCount", allMemberCount);
        model.addAttribute("todayMemberCount", todayMemberCount);
        model.addAttribute("todayMemberList", todayMemberList);
        model.addAttribute("accountStatusCounts", accountStatusCounts);
        model.addAttribute("countTodayNewBoard", countTodayNewBoard);
        model.addAttribute("monthlyActivityData", monthlyActivityData);


        return "admin/admin";
    }

    // 중복 실행 방지를 위한 상태 플래그
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    @PostMapping("/api/logstash/start")
    public ResponseEntity<?> startLogstash() {
        // 이미 실행 중이면 요청 거부
        if (isRunning.get()) {
            return ResponseEntity.status(429).body(Map.of(
                    "message", "Logstash 작업이 이미 실행 중입니다."
            ));
        }

        isRunning.set(true); // 실행 중으로 표시

        String logstashPath = "logstash"; // logstash 실행파일 경로
        String confPath = "/path/to/your-logstash.conf";        // conf 파일 경로

        ProcessBuilder processBuilder = new ProcessBuilder(logstashPath, "-f", confPath);
        processBuilder.redirectErrorStream(true); // 에러 스트림을 출력 스트림에 통합

        try {
            Process process = processBuilder.start();

            // 로그 출력 읽기 (비동기)
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[Logstash] " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Logstash 종료까지 대기 (동기 방식)
            int exitCode = process.waitFor();
            System.out.println("Logstash 종료: " + exitCode);

            return ResponseEntity.ok(Map.of(
                    "message", "Logstash 작업이 완료되었습니다.",
                    "exitCode", exitCode
            ));
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getMessage()
            ));
        } finally {
            // 무조건 실행 상태 해제
            isRunning.set(false);
        }

    }


    @ResponseBody
    @GetMapping("/api/allMember")
    public Map<String, Object> getAllMember(@PageableDefault(size = 10) Pageable pageable) {
        Page<MemberAdminDto> pageResult = MemberService.adminAllMember(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data", pageResult.getContent());       // 실제 내용
        response.put("total", pageResult.getTotalElements()); // 전체 개수
        response.put("totalPages", pageResult.getTotalPages()); // 전체 페이지 수
        response.put("page", pageResult.getNumber() + 1);     // 현재 페이지 번호 (1-based)
        return response;
    }

    @ResponseBody
    @GetMapping("/api/suspendedMember")
    public Map<String, Object> getSuspendedMember(@PageableDefault(size = 10) Pageable pageable) {
        Page<MemberAdminDto> pageResult = MemberService.adminSuspendedMember(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data", pageResult.getContent());       // 실제 내용
        response.put("total", pageResult.getTotalElements()); // 전체 개수
        response.put("totalPages", pageResult.getTotalPages()); // 전체 페이지 수
        response.put("page", pageResult.getNumber() + 1);     // 현재 페이지 번호 (1-based)
        return response;
    }


}
