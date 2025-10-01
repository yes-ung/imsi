<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>CheForest 로그인</title>
    <link rel="stylesheet" href="/css/auth/login.css">
</head>
<body>
<div class="login-container">
    <div class="login-content">
        <!-- 홈으로 돌아가기 -->
        <div class="back-link">
            <a href="/"><span>← 홈으로 돌아가기</span></a>
        </div>

        <div class="login-card">
            <!-- 헤더 -->
            <div class="card-header">
                <h2 class="card-title">CheForest</h2>
                <p class="card-description">요리와 함께하는 즐거운 시간을 시작하세요</p>
            </div>
            <!-- 에러 모달 -->
            <div id="errorModal" class="modal-overlay">
                <div class="modal-content">
                    <p>아이디 또는 비밀번호가 올바르지 않습니다.</p>
                    <button id="closeModalBtn">확인</button>
                </div>
            </div>
            <!-- 로그인 폼 -->
            <div class="card-content">
                <!-- ✅ Spring Security 맞춘 form -->
                <form id="loginForm" action="/auth/login" method="post">
                    <sec:csrfInput/>
                    <div class="form-group">
                        <label class="form-label">아이디</label>
                        <div class="input-wrapper">
                            <span class="input-icon">
                                <!-- 유저 아이콘 -->
                                <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                                    <path d="M12 12c2.7 0 5-2.3 5-5s-2.3-5-5-5-5 2.3-5 5 2.3 5 5 5zm0 2c-3.3 0-10 1.7-10 5v3h20v-3c0-3.3-6.7-5-10-5z"/>
                                </svg>
                            </span>
                            <input type="text" id="userId" name="loginId" class="form-input"
                                   placeholder="아이디를 입력하세요" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">비밀번호</label>
                        <div class="input-wrapper">
                            <span class="input-icon">
                                <!-- 자물쇠 아이콘 -->
                                <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                                    <path d="M17 8h-1V6c0-2.8-2.2-5-5-5S6 3.2 6 6v2H5c-1.1 0-2 .9-2
                                    2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9
                                    2-2V10c0-1.1-.9-2-2-2zm-5
                                    9c-1.1 0-2-.9-2-2s.9-2
                                    2-2 2 .9 2 2-.9 2-2 2zm3-9H9V6c0-1.7
                                    1.3-3 3-3s3 1.3 3 3v2z"/>
                                </svg>
                            </span>
                            <input type="password" id="password" name="password" class="form-input"
                                   placeholder="비밀번호를 입력하세요" required>
                        </div>
                    </div>

                    <button type="submit" id="submitBtn" class="submit-btn">
                        <span id="submitBtnText">로그인</span>
                    </button>
                </form>

                <!-- 아이디/비번 찾기 -->
                <div class="login-links">
                    <a href="/auth/find-id" class="auth-link">아이디를 잊으셨나요?</a>
                    <div class="divider-vertical"></div>
                    <a href="/auth/find-password" class="auth-link">비밀번호를 잊으셨나요?</a>
                </div>
            </div>

            <!-- 회원가입 -->
            <div class="card-footer">
                <div class="mode-switch">
                    <p class="switch-text">아직 계정이 없나요?</p>
                    <a href="/auth/register"><button class="switch-btn">회원가입하기</button></a>
                </div>

                <!-- 소셜 로그인 -->
                <div class="social-divider">
                    <div class="divider-line"></div>
                    <span class="divider-text">또는</span>
                    <div class="divider-line"></div>
                </div>
                <div class="social-buttons">
                    <!-- 구글 -->
                    <button class="social-btn google-btn"
                            onclick="location.href='/oauth2/authorization/google'">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 48 48">
                            <path fill="#EA4335" d="M24 9.5c3.9 0 6.6 1.7 8.1 3.1l6-5.9C34.6 3.6 29.7 1.5 24 1.5 14.9 1.5 7.1 7.3 4.1 15.2l7.1 5.5C12.9 15.1 18 9.5 24 9.5z"/>
                            <path fill="#4285F4" d="M46.1 24.5c0-1.6-.1-3.2-.4-4.7H24v9.1h12.6c-.5 2.7-2 5-4.2 6.5l6.5 5.1c3.8-3.5 6.2-8.7 6.2-15z"/>
                            <path fill="#FBBC05" d="M11.2 28.9c-1-2.7-1-5.6 0-8.3L4.1 15c-2.5 5-2.5 11 0 16l7.1-5.1z"/>
                            <path fill="#34A853" d="M24 46.5c5.7 0 10.6-1.9 14.1-5.2l-6.5-5.1c-2 1.4-4.6 2.3-7.6 2.3-6 0-11.1-5.6-12.8-11.1l-7.1 5.5C7.1 40.7 14.9 46.5 24 46.5z"/>
                        </svg>
                    </button>
                    <!-- 카카오 -->
                    <button class="social-btn kakao-btn"
                            onclick="location.href='/oauth2/authorization/kakao'">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 32">
                            <path fill="#3C1E1E" d="M16 3C8.8 3 3 7.9 3 14c0 3.5 2.1 6.6 5.5 8.6-.2.7-.9 3.3-1 3.8 0 0-.1.5.3.3.4-.2 3.8-2.5 4.4-2.9.9.1 1.7.2 2.8.2 7.2 0 13-4.9 13-11S23.2 3 16 3z"/>
                        </svg>
                    </button>
                    <!-- 네이버 -->
                    <button class="social-btn naver-btn"
                            onclick="location.href='/oauth2/authorization/naver'">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 32 32">
                            <path fill="#03C75A" d="M7 5h6.3l5.7 8.8V5H25v22h-6.3l-5.7-8.8V27H7z"/>
                        </svg>
                    </button>
                </div>
            </div>
        </div>

        <!-- 등급 시스템 링크 -->
        <div class="grade-link">
            <a href="/grade" class="grade-btn">CheForest 등급 시스템 알아보기 →</a>
        </div>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const errorParam = new URLSearchParams(window.location.search).get("error");
        const errorModal = document.getElementById("errorModal");
        const closeModalBtn = document.getElementById("closeModalBtn");

        if (errorParam && errorModal) {
            errorModal.classList.add("active");
        }

        if (closeModalBtn) {
            closeModalBtn.addEventListener("click", function() {
                errorModal.classList.remove("active");
            });
        }
    });
</script>

</body>
</html>
