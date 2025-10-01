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
            Object socialIdObj = oAuth2User.getAttributes().get("id");   // Kakao → Long
            String socialId = (socialIdObj != null) ? String.valueOf(socialIdObj) : null;

            Object providerObj = oAuth2User.getAttributes().get("provider");
            String provider = (providerObj instanceof String) ? (String) providerObj : null;

            if (socialId != null && provider != null) {
                member = memberRepository
                        .findBySocialIdAndProvider(socialId, provider.toUpperCase())
                        .orElse(null);

                request.getSession().setAttribute("provider", provider.toUpperCase());
            }
        }

        // ✅ 자동 생성된 닉네임이면 모달용 세션 저장
        if (member != null && member.getNickname() != null && member.getNickname().contains("_")) {
            String originalNickname = member.getNickname().split("_")[0];
            request.getSession().setAttribute("originalNickname", originalNickname);

            log.info("자동 생성된 닉네임 감지 → 모달 표시 준비 (원래 닉네임: {}, 현재 닉네임: {})",
                    originalNickname, member.getNickname());

            // 홈으로 이동 → JSP에서 모달 표시
            response.sendRedirect("/");
            return;
        }

        // ✅ SavedRequest 확인 (원래 요청 페이지로 복귀)
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);

        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
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
