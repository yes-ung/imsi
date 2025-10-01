// ==============================
// 전역 함수 (JSP inline 이벤트에서도 호출 가능)
// ==============================
function clearError(fieldId) {
    const errorEl = document.getElementById(fieldId + "Error");
    if (errorEl) {
        errorEl.style.display = "none";
        const span = errorEl.querySelector("span");
        if (span) span.textContent = "";
    }
}

// ==============================
// CheForest 회원가입 JavaScript (실시간 검증 + AJAX 방식)
// ==============================
document.addEventListener("DOMContentLoaded", function () {
    const signupForm = document.getElementById("signupForm");

    // 입력 필드
    const userIdInput = document.getElementById("userId");
    const emailInput = document.getElementById("email");
    const emailCodeInput = document.getElementById("emailCode");
    const passwordInput = document.getElementById("password");
    const confirmPasswordInput = document.getElementById("confirmPassword");
    const nicknameInput = document.getElementById("nickname");

    // 그룹/메시지
    const emailVerificationSection = document.getElementById("emailVerificationSection");
    const emailSuccess = document.getElementById("emailSuccess");
    const emailSentMessage = document.getElementById("emailSentMessage"); // ✅ 추가
    const nicknameSuccess = document.getElementById("nicknameSuccess");
    const userIdSuccess = document.getElementById("userIdSuccess");

    // 버튼
    const sendEmailBtn = document.getElementById("sendEmailBtn");
    const verifyEmailBtn = document.getElementById("verifyEmailBtn");

    // 토스트/모달
    const successModal = document.getElementById("successModal");
    const modalTitle = document.getElementById("modalTitle");
    const modalMessage = document.getElementById("modalMessage");
    const toast = document.getElementById("toast");
    const toastMessage = document.getElementById("toastMessage");

    // 상태 변수
    let verificationState = {
        userIdChecked: false,
        emailSent: false,
        emailVerified: false,
        nicknameChecked: false,
    };

    // CSRF 토큰 (Spring Security)
    const csrfToken = document.querySelector("meta[name='_csrf']")?.content;
    const csrfHeader = document.querySelector("meta[name='_csrf_header']")?.content;

    // // ==============================
    // // AJAX 공통 요청
    // // ==============================
    // async function ajaxRequest(url, method, data) {
    //     const headers = { "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8" };
    //     if (csrfToken && csrfHeader) headers[csrfHeader] = csrfToken;
    //
    //     let fetchOptions = { method, headers };
    //     if (method.toUpperCase() === "GET") {
    //         if (data) {
    //             const params = new URLSearchParams(data).toString();
    //             url += (url.includes("?") ? "&" : "?") + params;
    //         }
    //     } else {
    //         fetchOptions.body = new URLSearchParams(data).toString();
    //     }
    //
    //     const response = await fetch(url, fetchOptions);
    //     const contentType = response.headers.get("content-type");
    //
    //     if (contentType && contentType.includes("application/json")) {
    //         return await response.json();
    //     } else {
    //         return await response.text();
    //     }
    // }

    // ==============================
    // 실시간 검증: 아이디
    // ==============================
    let userIdTimer;
    userIdInput.addEventListener("keyup", () => {
        clearTimeout(userIdTimer);
        userIdTimer = setTimeout(async () => {
            const id = userIdInput.value.trim();

            hideElement(userIdSuccess);
            clearError("userId");

            if (id.length < 8) {
                showError("userId", "아이디는 최소 8자 이상이어야 합니다.");
                verificationState.userIdChecked = false;
                return;
            }

            const available = await ajaxRequest("/auth/check-id", "GET", { loginId: id });
            if (available === true) {
                verificationState.userIdChecked = true;
                showElement(userIdSuccess);
                clearError("userId");
            } else {
                verificationState.userIdChecked = false;
                hideElement(userIdSuccess);
                showError("userId", "이미 사용중인 아이디입니다.");
            }
        }, 200);
    });

    // ==============================
    // 실시간 검증: 닉네임
    // ==============================
    let nicknameTimer;
    nicknameInput.addEventListener("keyup", () => {
        clearTimeout(nicknameTimer);
        nicknameTimer = setTimeout(async () => {
            const nickname = nicknameInput.value.trim();

            hideElement(nicknameSuccess);
            clearError("nickname");

            if (nickname.length < 2) {
                showError("nickname", "닉네임은 최소 2자 이상이어야 합니다.");
                verificationState.nicknameChecked = false;
                return;
            }

            const available = await ajaxRequest("/auth/check-nickname", "GET", { nickname });
            if (available === true) {
                verificationState.nicknameChecked = true;
                showElement(nicknameSuccess);
                clearError("nickname");
            } else {
                verificationState.nicknameChecked = false;
                hideElement(nicknameSuccess);
                showError("nickname", "이미 사용중인 닉네임입니다.");
            }
        }, 200);
    });

    // ==============================
    // 실시간 검증: 비밀번호
    // ==============================
    passwordInput.addEventListener("keyup", () => {
        const password = passwordInput.value.trim();
        const pwPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()_+\-={}[\]|:;"'<>,.?/]).{10,20}$/;
        if (!pwPattern.test(password)) {
            showError("password", "비밀번호는 영문, 숫자, 특수문자를 포함해 10~20자로 입력하세요.");
        } else {
            clearError("password");
        }
    });

    // ==============================
    // 실시간 검증: 비밀번호 확인
    // ==============================
    confirmPasswordInput.addEventListener("keyup", () => {
        if (passwordInput.value !== confirmPasswordInput.value) {
            showError("confirmPassword", "비밀번호가 일치하지 않습니다.");
        } else {
            clearError("confirmPassword");
        }
    });

    // ==============================
    // 실시간 검증: 이메일
    // ==============================
    emailInput.addEventListener("keyup", () => {
        const email = emailInput.value.trim();
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(email)) {
            showError("email", "올바른 이메일 형식이 아닙니다.");
            sendEmailBtn.disabled = true;
        } else {
            clearError("email");
            sendEmailBtn.disabled = false;
        }
    });

    // ==============================
    // 이메일 인증 발송
    // ==============================
    if (sendEmailBtn) {
        sendEmailBtn.addEventListener("click", async () => {
            const email = emailInput.value.trim();
            if (!email) return showToast("이메일을 입력해주세요.", "error");

            const result = await ajaxRequest("/auth/send-email-code", "POST", { email });
            console.log("send-email-code result:", result);

            if (String(result).trim().toUpperCase() === "OK") {
                verificationState.emailSent = true;
                showElement(emailVerificationSection);
                showElement(emailSentMessage); // ✅ 안내 문구 표시
                showToast("인증번호가 발송되었습니다.", "success");
            } else {
                showToast("이메일 발송 실패: " + result, "error");
            }
        });
    }

    // ==============================
    // 이메일 인증 확인
    // ==============================
    if (verifyEmailBtn) {
        verifyEmailBtn.addEventListener("click", async () => {
            const code = emailCodeInput.value.trim();
            if (!code) return showToast("인증번호를 입력해주세요.", "error");

            const verified = await ajaxRequest("/auth/verify-email-code", "POST", { code });
            if (verified === true) {
                verificationState.emailVerified = true;
                showElement(emailSuccess);
                showToast("이메일 인증이 완료되었습니다.", "success");
            } else {
                showToast("인증번호가 일치하지 않습니다.", "error");
            }
        });
    }

// ==============================
// 회원가입 처리
// ==============================
    if (signupForm) {
        signupForm.addEventListener("submit", async function (e) {
            e.preventDefault();
            if (!validateSignupForm()) return;

            const formData = {
                loginId: userIdInput.value.trim(),
                email: emailInput.value.trim(),
                emailAuthCode: emailCodeInput.value.trim(),
                password: passwordInput.value.trim(),
                confirmPassword: confirmPasswordInput.value.trim(),
                nickname: nicknameInput.value.trim(),
            };

            try {
                const result = await ajaxRequest("/auth/register/addition", "POST", formData);
                console.log("register result:", result);

                if (String(result).trim().toUpperCase() === "OK") {
                    showModal("회원가입 완료!", "성공적으로 가입되었습니다. 로그인 페이지로 이동합니다.");

                    // ✅ 확인 버튼 클릭 시 로그인 페이지로 이동
                    const okBtn = document.getElementById("modalOkBtn");
                    if (okBtn) {
                        okBtn.onclick = () => {
                            window.location.href = "/auth/login";
                        };
                    }
                } else {
                    showToast(result, "error");
                }
            } catch (err) {
                console.error(err);
                showToast("서버 오류가 발생했습니다.", "error");
            }
        });
    }

    // ==============================
    // 유틸리티
    // ==============================
    function validateSignupForm() {
        if (!verificationState.userIdChecked) return showToast("아이디를 확인해주세요.", "error"), false;
        if (!verificationState.nicknameChecked) return showToast("닉네임을 확인해주세요.", "error"), false;
        if (!verificationState.emailVerified) return showToast("이메일 인증을 완료해주세요.", "error"), false;
        if (passwordInput.value !== confirmPasswordInput.value) return showToast("비밀번호가 일치하지 않습니다.", "error"), false;
        return true;
    }

    function showError(fieldId, message) {
        const errorEl = document.getElementById(fieldId + "Error");
        if (errorEl) {
            const span = errorEl.querySelector("span");
            if (span) span.textContent = message;
            errorEl.style.display = "block";
        }
    }

    function showElement(el) { if (el) el.style.display = "block"; }
    function hideElement(el) { if (el) el.style.display = "none"; }

    function showModal(title, message) {
        if (modalTitle) modalTitle.textContent = title;
        if (modalMessage) modalMessage.textContent = message;
        if (successModal) successModal.style.display = "flex";
    }

    function showToast(message, type = "info") {
        if (!toast) return alert(message);
        toastMessage.textContent = message;
        toast.className = `toast ${type}`;
        toast.style.display = "block";
        setTimeout(() => (toast.style.display = "none"), 3000);
    }
});
