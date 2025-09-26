package com.simplecoding.cheforest.jpa.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id:}")
    private String kakaoClientId;

    // (선택) 우리 서비스 도메인 – 절대경로 검증에 사용할 수 있음
    @Value("${app.domain:https://your-domain.example}")
    private String appDomain;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {

        // 1) provider 결정: 세션 값 우선, 없으면 Authentication에서 추론
        String provider = (String) safeGetSessionAttr(request, "provider");
        if (provider == null && authentication instanceof OAuth2AuthenticationToken oAuth2) {
            provider = oAuth2.getAuthorizedClientRegistrationId(); // kakao/google/naver 등
            if (provider != null) provider = provider.toUpperCase();
        }

        // 2) redirect 후보: ?redirect=... > Referer > "/"
        String redirect = trimToNull(request.getParameter("redirect"));
        if (redirect == null) redirect = trimToNull(request.getHeader("Referer"));
        if (!isSafeRedirect(redirect)) redirect = "/";

        // 3) 세션 무효화 + 쿠키 제거
        var session = request.getSession(false);
        if (session != null) session.invalidate();
        deleteCookie("JSESSIONID", request, response);
        deleteCookie("remember-me", request, response); // 사용하지 않으면 삭제돼도 문제 없음

        // 4) Provider별 로그아웃 URL 구성
        String providerLogoutUrl = null;
        if ("KAKAO".equalsIgnoreCase(provider) && notBlank(kakaoClientId)) {
            // Kakao는 developers 콘솔에 logout_redirect_uri 등록되어 있어야 합니다.
            String encoded = URLEncoder.encode(toAbsoluteRedirect(redirect), StandardCharsets.UTF_8);
            providerLogoutUrl = "https://kauth.kakao.com/oauth/logout"
                    + "?client_id=" + kakaoClientId
                    + "&logout_redirect_uri=" + encoded;
        } else if ("NAVER".equalsIgnoreCase(provider)) {
            // 단순 포털 로그아웃 세션 정리 – 토큰 해지는 별도 API 필요(필요시 추후 추가)
            providerLogoutUrl = "https://nid.naver.com/nidlogin.logout";
            // 네이버에서 로그아웃 후 우리 서비스로 자동 복귀가 보장되진 않음 → 이후 브라우저 뒤로가기로 복귀될 수 있음
        } else if ("GOOGLE".equalsIgnoreCase(provider)) {
            // Google은 공식적인 단일 로그아웃 URL이 없습니다(사용자 전체 로그아웃/토큰 revoke는 별도).
            // 일반적으로는 우리 서비스 세션만 종료하고 로컬로 리다이렉트합니다.
            providerLogoutUrl = null;
        }

        // 5) 최종 이동
        if (providerLogoutUrl != null) {
            response.sendRedirect(providerLogoutUrl);
        } else {
            response.sendRedirect(redirect);
        }
    }

    // ----- helper methods -----

    private static Object safeGetSessionAttr(HttpServletRequest req, String key) {
        var session = req.getSession(false);
        return (session == null) ? null : session.getAttribute(key);
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    /** 상대 경로만 허용: "/", "/xxx" 형태. 절대 URL, 프로토콜, // 형태는 차단 */
    private static boolean isSafeRedirect(String url) {
        if (url == null) return false;
        // query/hash만 오는 경우 처리
        if (url.startsWith("?") || url.startsWith("#")) return true;
        // 절대 URL, 프로토콜 상대 URL 차단
        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("//")) return false;
        // 루트 시작만 허용
        return url.startsWith("/");
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    /** Kakao logout_redirect_uri는 절대 URL 권장 – 필요 시 우리 도메인과 합쳐줌 */
    private String toAbsoluteRedirect(String redirect) {
        if (redirect == null) return appDomain + "/";
        // 이미 절대 URL이면 그대로(원한다면 동일 도메인만 허용하도록 추가 검증 가능)
        if (redirect.startsWith("http://") || redirect.startsWith("https://")) return redirect;
        // 상대경로면 our domain 붙이기
        String base = appDomain.endsWith("/") ? appDomain.substring(0, appDomain.length()-1) : appDomain;
        return base + redirect;
    }

    private static void deleteCookie(String name, HttpServletRequest req, HttpServletResponse res) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        // SameSite 설정은 Servlet API에서 직접 제어가 어려워 Set-Cookie 헤더로 처리하는 방법도 있음
        cookie.setHttpOnly(true);
        res.addCookie(cookie);
    }
}
