package com.simplecoding.cheforest.jpa.auth.controller;

import com.simplecoding.cheforest.jpa.auth.dto.MemberDetailDto;
import com.simplecoding.cheforest.jpa.auth.dto.MemberSignupDto;
import com.simplecoding.cheforest.jpa.auth.dto.MemberUpdateDto;
import com.simplecoding.cheforest.jpa.auth.entity.Member;
import com.simplecoding.cheforest.jpa.auth.repository.MemberRepository;
import com.simplecoding.cheforest.jpa.auth.security.CustomOAuth2User;
import com.simplecoding.cheforest.jpa.auth.service.MemberService;
import com.simplecoding.cheforest.jpa.auth.service.EmailService;
import com.simplecoding.cheforest.jpa.auth.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final MemberRepository memberRepository;

    // ================= 로그인 페이지 =================
    @GetMapping("/auth/login")
    public String loginView() {
        return "auth/login";
    }

    // ================= 회원가입 페이지 =================
    @GetMapping("/auth/register")
    public String registerView(Model model) {
        model.addAttribute("memberSignupDto", new MemberSignupDto());
        return "auth/signup";
    }

    // ================= 회원가입 처리 =================
    @PostMapping("/auth/register/addition")
    @ResponseBody
    public String register(@Valid @ModelAttribute MemberSignupDto dto,
                           BindingResult bindingResult,
                           @SessionAttribute(name = "emailAuthCode", required = false) String serverAuthCode,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "❌ 입력값을 확인해주세요.";
        }
        try {
            memberService.register(dto, serverAuthCode);
            return "OK"; // ✅ 가입 성공 시 문자열만 반환
        } catch (IllegalArgumentException e) {
            return "❌ " + e.getMessage();
        }
    }

    // ================= 회원정보 수정 페이지 =================
    @GetMapping("/auth/update")
    public String updateView(@AuthenticationPrincipal CustomUserDetails user, Model model) {
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("memberUpdateDto", new MemberUpdateDto());
        return "mypage/edit";
    }

    // ================= 회원정보 수정 처리 =================
    @PostMapping("/auth/update")
    public String update(@Valid @ModelAttribute MemberUpdateDto dto,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal CustomUserDetails user,
                         Model model) {
        if (bindingResult.hasErrors()) {
            return "mypage/edit";
        }
        try {
            memberService.update(dto);
            model.addAttribute("msg", "회원정보가 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", "❌ " + e.getMessage());
            return "mypage/edit";
        }
        return "mypage/edit";
    }

//    회원탈퇴
    @PostMapping("/member/withdraw")
    public String withdraw(@AuthenticationPrincipal CustomUserDetails user,
                           RedirectAttributes ra) {

        Long memberIdx = user.getMember().getMemberIdx();
        memberService.withdraw(memberIdx);

        // ✅ Spring Security 인증정보 삭제 (로그아웃 처리)
        SecurityContextHolder.clearContext();

        ra.addFlashAttribute("msg", "회원 탈퇴가 완료되었습니다.");
        return "redirect:/";
    }

    // ================= 회원 상세 조회 =================
    @GetMapping("/auth/detail/{id}")
    public String detail(@PathVariable("id") Long memberIdx, Model model) {
        MemberDetailDto detail = memberService.findById(memberIdx);
        model.addAttribute("member", detail);
        return "auth/detail";
    }

    // ================= Ajax: 중복체크 & 이메일 =================
    @GetMapping("/auth/check-id")
    @ResponseBody
    public boolean checkId(@RequestParam String loginId) {
        return !memberService.existsByLoginId(loginId);
    }

    @GetMapping("/auth/check-nickname")
    @ResponseBody
    public boolean checkNickname(@RequestParam String nickname) {
        return !memberService.existsByNickname(nickname);
    }

    @PostMapping("/auth/send-email-code")
    @ResponseBody
    public String sendEmailCode(@RequestParam String email,
                                HttpSession session) {
        String newCode = emailService.sendAuthCode(email);
        session.setAttribute("emailAuthCode", newCode);
        return "OK";
    }

    @PostMapping("/auth/verify-email-code")
    @ResponseBody
    public boolean verifyEmailCode(@RequestParam String code,
                                   @SessionAttribute(name = "emailAuthCode", required = false) String serverCode) {
        return serverCode != null && serverCode.equals(code);
    }

    // ================= 아이디 찾기 페이지 =================
    @GetMapping("/auth/find-id")
    public String findIdView() {
        return "auth/findId"; // JSP 위치 (예: /WEB-INF/views/auth/findId.jsp)
    }

    // ================= 아이디 찾기: 인증번호 발송 =================
    @PostMapping("/auth/find-id/send-code")
    @ResponseBody
    public String sendFindIdCode(@RequestParam String email, HttpSession session) {
        // 1) 이메일 존재 여부 확인
        if (!memberService.existsByEmail(email)) {
            return "❌ 가입된 계정을 찾을 수 없습니다.";
        }

        // 2) 인증번호 발송
        String newCode = emailService.sendAuthCode(email);

        // 3) 세션에 인증번호 저장
        session.setAttribute("findIdAuthCode", newCode);
        session.setAttribute("findIdEmail", email);

        return "OK";
    }

    // ================= 아이디 찾기: 인증번호 확인 =================
    @PostMapping("/auth/find-id/verify-code")
    @ResponseBody
    public String verifyFindIdCode(@RequestParam String code,
                                   @SessionAttribute(name = "findIdAuthCode", required = false) String serverCode,
                                   @SessionAttribute(name = "findIdEmail", required = false) String email,
                                   HttpSession session) {

        if (serverCode == null || email == null) {
            return "FAIL";
        }

        if (!serverCode.equals(code)) {
            return "FAIL";
        }

        String loginId = memberService.findLoginIdByEmail(email);

        // ✅ 인증번호 세션만 정리
        session.removeAttribute("findIdAuthCode");
        session.removeAttribute("findIdEmail");

        return (loginId != null) ? loginId : "FAIL";
    }

    // ================= 비밀번호 찾기 페이지 =================
    @GetMapping("/auth/find-password")
    public String findPasswordView() {
        return "auth/findPassword"; // JSP 위치
    }

    // ================= 비밀번호 찾기: 인증번호 발송 =================
    @PostMapping("/auth/find-password/send-code")
    @ResponseBody
    public String sendPasswordResetCode(@RequestParam String loginId,
                                        @RequestParam String email,
                                        HttpSession session) {
        try {
            memberService.sendPasswordResetCode(loginId, email, session);
            return "OK";
        } catch (IllegalArgumentException e) {
            return "❌ " + e.getMessage();
        }
    }

    // ================= 비밀번호 찾기: 인증번호 검증 =================
    @PostMapping("/auth/find-password/verify-code")
    @ResponseBody
    public String verifyPasswordResetCode(@RequestParam String code,
                                          HttpSession session) {
        try {
            memberService.verifyPasswordResetCode(code, session);
            return "OK";
        } catch (IllegalArgumentException e) {
            return "❌ " + e.getMessage();
        }
    }

    // ================= 비밀번호 찾기: 비밀번호 재설정 =================
    @PostMapping("/auth/find-password/reset")
    @ResponseBody
    public String resetPassword(@RequestParam String newPassword,
                                HttpSession session) {
        try {
            memberService.resetPassword(newPassword, session);
            return "OK";
        } catch (IllegalArgumentException e) {
            return "❌ " + e.getMessage();
        }
    }

    // ✅ 소셜 로그인 시 중복 닉네임 수정
    @PostMapping("/auth/nickname/update")
    public String updateSocialNickname(@RequestParam String nickname,
                                       RedirectAttributes ra) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 체크
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            ra.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/auth/login";
        }

        // 👉 소셜 로그인 전용 처리
        if (!(auth.getPrincipal() instanceof CustomOAuth2User oauth2User)) {
            ra.addFlashAttribute("msg", "소셜 로그인 사용자만 변경 가능합니다.");
            return "redirect:/";
        }

        Member member = oauth2User.getMember();

        // 닉네임 유효성 검사
        if (nickname == null || nickname.trim().isEmpty()) {
            ra.addFlashAttribute("msg", "닉네임은 필수입니다.");
            return "redirect:/";
        }
        if (memberRepository.existsByNickname(nickname)) {
            ra.addFlashAttribute("msg", "이미 사용 중인 닉네임입니다.");
            return "redirect:/";
        }

        // DB 저장
        member.setNickname(nickname);
        memberRepository.save(member);

        // SecurityContext 갱신
        CustomOAuth2User updatedUser =
                new CustomOAuth2User(member, oauth2User.getAttributes());

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedUser, null, updatedUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        ra.addFlashAttribute("msg", "닉네임이 변경되었습니다.");
        return "redirect:/";
    }

}
