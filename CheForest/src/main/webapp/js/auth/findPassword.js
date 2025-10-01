// CheForest 비밀번호 찾기 페이지 JavaScript

document.addEventListener('DOMContentLoaded', function() {
    initializeFindPasswordPage();
});

// 페이지 초기화
function initializeFindPasswordPage() {
    const btnBack = document.getElementById('btn-back');
    const btnSendCode = document.getElementById('btn-send-code');
    const btnVerifyCode = document.getElementById('btn-verify-code');
    const emailCodeInput = document.getElementById('email-code');
    const resetForm = document.getElementById('reset-form');

    // 뒤로가기
    if (btnBack) {
        btnBack.addEventListener('click', () => goToLogin());
    }

    // 인증번호 발송
    if (btnSendCode) {
        btnSendCode.addEventListener('click', sendEmailCode);
    }

    // 인증번호 확인
    if (btnVerifyCode) {
        btnVerifyCode.addEventListener('click', verifyEmailCode);
    }

    // 인증번호 엔터 입력
    if (emailCodeInput) {
        emailCodeInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') verifyEmailCode();
        });
    }

    // 새 비밀번호 제출
    if (resetForm) {
        resetForm.addEventListener('submit', function(e) {
            e.preventDefault();
            submitNewPassword();
        });
    }

    console.log("비밀번호 찾기 페이지 초기화 완료");
}

// ---------------- 인증 관련 ----------------

// 이메일 인증번호 발송
function sendEmailCode() {
    const userId = document.getElementById('user-id').value.trim();
    const email = document.getElementById('email').value.trim();

    if (!validateUserId(userId) || !validateEmail(email)) return;

    showLoadingState('btn-send-code');

    fetch("/auth/find-password/send-code", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({ loginId: userId, email: email })
    })
        .then(res => res.text())
        .then(result => {
            hideLoadingState('btn-send-code');
            if (result === "OK") {
                showCodeInput();
                updateSendCodeButtonText('발송완료');
                showModal("인증번호가 발송되었습니다. 이메일을 확인해주세요.");
            } else {
                showModal(result);
            }
        })
        .catch(err => {
            hideLoadingState('btn-send-code');
            console.error(err);
            showModal("서버 오류가 발생했습니다.");
        });
}

// 인증번호 확인
function verifyEmailCode() {
    const code = document.getElementById('email-code').value.trim();
    if (!validateCode(code)) return;

    showLoadingState('btn-verify-code');

    fetch("/auth/find-password/verify-code", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({ code: code })
    })
        .then(res => res.text())
        .then(result => {
            hideLoadingState('btn-verify-code');
            if (result.trim().toUpperCase() === "OK") {
                showModal("이메일 인증이 완료되었습니다. 새 비밀번호를 설정해주세요.");
                showResetStep();
            } else {
                showModal(result);
            }
        })
        .catch(err => {
            hideLoadingState('btn-verify-code');
            console.error(err);
            showModal("서버 오류가 발생했습니다.");
        });
}

// ---------------- 비밀번호 재설정 ----------------

// 새 비밀번호 입력 단계로 전환
function showResetStep() {
    const verificationStep = document.getElementById('verification-step');
    const resetStep = document.getElementById('reset-step');
    const cardFooter = document.getElementById('card-footer');

    if (verificationStep) verificationStep.style.display = 'none';
    if (cardFooter) cardFooter.style.display = 'none';

    if (resetStep) resetStep.style.display = 'block';
}

// 새 비밀번호 제출
function submitNewPassword() {
    const newPw = document.getElementById('new-password').value.trim();
    const confirmPw = document.getElementById('confirm-password').value.trim();

    if (!newPw || newPw.length < 8) {
        showModal("비밀번호는 8자 이상이어야 합니다.");
        return;
    }
    if (newPw !== confirmPw) {
        showModal("비밀번호가 일치하지 않습니다.");
        return;
    }

    fetch("/auth/find-password/reset", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({ newPassword: newPw })
    })
        .then(res => res.text())
        .then(result => {
            if (result === "OK") {
                showModal("비밀번호가 성공적으로 변경되었습니다. 로그인 페이지로 이동합니다.");
                setTimeout(() => { window.location.href = "/auth/login"; }, 2000);
            } else {
                showModal(result);
            }
        })
        .catch(err => {
            console.error(err);
            showModal("서버 오류가 발생했습니다.");
        });
}

// ---------------- 유틸 ----------------

// 인증번호 입력란 표시
function showCodeInput() {
    const codeGroup = document.getElementById('code-group');
    if (codeGroup) codeGroup.style.display = 'flex';
}

// 입력값 검증
function validateUserId(userId) {
    if (!userId) { showModal("아이디를 입력해주세요."); return false; }
    if (userId.length < 4) { showModal("아이디는 4자 이상이어야 합니다."); return false; }
    return true;
}
function validateEmail(email) {
    if (!email) { showModal("이메일을 입력해주세요."); return false; }
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!regex.test(email)) { showModal("올바른 이메일 형식이 아닙니다."); return false; }
    return true;
}
function validateCode(code) {
    if (!code) { showModal("인증번호를 입력해주세요."); return false; }
    if (code.length !== 6) { showModal("인증번호는 6자리여야 합니다."); return false; }
    return true;
}

// 모달 표시
function showModal(message) {
    const modal = document.getElementById("alertModal");
    const msg = document.getElementById("alertModalMessage");
    const closeBtn = document.getElementById("alertModalClose");

    if (modal && msg) {
        msg.textContent = message;
        modal.style.display = "block";
    }
    if (closeBtn) {
        closeBtn.onclick = () => { modal.style.display = "none"; };
    }
    window.onclick = (e) => {
        if (e.target === modal) modal.style.display = "none";
    };
}

// 로딩 상태 토글
function showLoadingState(buttonId) {
    const btn = document.getElementById(buttonId);
    if (btn) { btn.classList.add("loading"); btn.disabled = true; }
}
function hideLoadingState(buttonId) {
    const btn = document.getElementById(buttonId);
    if (btn) { btn.classList.remove("loading"); btn.disabled = false; }
}

// 버튼 텍스트 변경
function updateSendCodeButtonText(text) {
    const btn = document.getElementById('btn-send-code');
    const span = btn.querySelector('.btn-text');
    if (span) span.textContent = text;
}

// 로그인 페이지 이동
function goToLogin() {
    window.location.href = "/auth/login";
}
