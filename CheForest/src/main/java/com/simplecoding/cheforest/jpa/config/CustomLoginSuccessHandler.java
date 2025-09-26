package com.simplecoding.cheforest.jpa.config;

import com.simplecoding.cheforest.jpa.auth.entity.Member;
import com.simplecoding.cheforest.jpa.auth.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Object principal = authentication.getPrincipal();
        Member member = null;

        // ✅ 일반 로그인
        if (principal instanceof UserDetails userDetails) {
            String loginId = userDetails.getUsername();
            member = memberRepository.findByLoginId(loginId).orElse(null);
            request.getSession().setAttribute("provider", "LOCAL");
        }
        // ✅ 소셜 로그인
        else if (principal instanceof OAuth2User oAuth2User) {
            String email = (String) oAuth2User.getAttributes().get("email");
            member = memberRepository.findByEmail(email).orElse(null);

            String provider = (String) oAuth2User.getAttributes().get("provider");
            if (provider != null) {
                request.getSession().setAttribute("provider", provider.toUpperCase());
            }
        }

        // ✅ 닉네임 중복 체크
        if (member != null && memberRepository.existsByNicknameAndMemberIdxNot(
                member.getNickname(), member.getMemberIdx())) {
            log.info("닉네임 중복 발생 → 닉네임 변경 페이지로 이동");
            response.sendRedirect("/auth/nickname/change");
            return;
        }

// ✅ SavedRequest 확인
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);

        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();

            // 🚨 /error 로 시작하면 홈으로 강제 이동
            if (targetUrl != null && !targetUrl.contains("/error")) {
                log.info("로그인 성공 → 원래 요청한 페이지로 이동: {}", targetUrl);
                response.sendRedirect(targetUrl);
            } else {
                log.info("로그인 성공 → 잘못된 redirectUrl 감지, 홈으로 이동");
                response.sendRedirect("/");
            }
        } else {
            log.info("로그인 성공 → 기본 홈으로 이동");
            response.sendRedirect("/");
        }
    }
}
