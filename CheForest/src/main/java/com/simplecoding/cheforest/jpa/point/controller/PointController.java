package com.simplecoding.cheforest.jpa.point.controller;

import com.simplecoding.cheforest.jpa.auth.entity.Member;
import com.simplecoding.cheforest.jpa.auth.security.CustomUserDetails;
import com.simplecoding.cheforest.jpa.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @GetMapping("/grade")
    public String gradePage(@AuthenticationPrincipal CustomUserDetails loginUser, Model model) {

        if (loginUser != null) {
            // 로그인 상태일 때만 포인트 계산 로직 수행
            Member member = loginUser.getMember();
            Long todayPoints = pointService.getTodayPoints(member.getMemberIdx());
            Long nextGradePoint = pointService.getNextGradePoint(member.getPoint());

            // member는 JSP에서 직접 가져오므로 모델에 추가할 필요 없음
            model.addAttribute("todayPoints", todayPoints);
            model.addAttribute("nextGradePoint", nextGradePoint);

            if (member.getInsertTime() != null) {
                String joinDate = member.getInsertTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
                model.addAttribute("joinDate", joinDate);
            }
        }

        return "grade";
    }
}
