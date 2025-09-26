// CheForest 회원가입 JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // DOM 요소들
    const backBtn = document.getElementById('backBtn');
    const registerForm = document.getElementById('registerForm');
    const submitBtn = document.getElementById('submitBtn');
    const submitBtnText = document.getElementById('submitBtnText');

    // 입력 필드
    const userIdInput = document.getElementById('userId');
    const emailInput = document.getElementById('email');
    const emailCodeInput = document.getElementById('emailCode');
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const nicknameInput = document.getElementById('nickname');

    // 그룹
    const emailCodeGroup = document.getElementById('emailCodeGroup');

    // 버튼
    const sendEmailBtn = document.getElementById('sendEmailBtn');
    const verifyEmailBtn = document.getElementById('verifyEmailBtn');
    const checkNicknameBtn = document.getElementById('checkNicknameBtn');

    // 메시지
    const emailVerifiedMessage = document.getElementById('emailVerifiedMessage');
    const passwordMatchMessage = document.getElementById('passwordMatchMessage');
    const nicknameCheckedMessage = document.getElementById('nicknameCheckedMessage');

    // 모달 및 알림
    const successModal = document.getElementById('successModal');
    const modalTitle = document.getElementById('modalTitle');
    const modalMessage = document.getElementById('modalMessage');
    const modalOkBtn = document.getElementById('modalOkBtn');
    const loadingOverlay = document.getElementById('loadingOverlay');
    const toast = document.getElementById('toast');
    const toastIcon = document.getElementById('toastIcon');
    const toastMessage = document.getElementById('toastMessage');

    // 상태 변수
    let verificationState = {
        emailSent: false,
        emailVerified: false,
        nicknameChecked: false
    };

    // 뒤로가기
    if (backBtn) {
        backBtn.addEventListener('click', () => {
            // TODO: 홈페이지 이동 구현
            window.location.href = '/home';
        });
    }

    // 폼 제출
    if (registerForm) {
        registerForm.addEventListener('submit', handleSignup);
    }

    // 이메일 인증 발송
    if (sendEmailBtn) {
        sendEmailBtn.addEventListener('click', handleSendEmailCode);
    }

    // 이메일 인증 확인
    if (verifyEmailBtn) {
        verifyEmailBtn.addEventListener('click', handleVerifyEmailCode);
    }

    // 닉네임 중복확인
    if (checkNicknameBtn) {
        checkNicknameBtn.addEventListener('click', handleCheckNickname);
    }

    // 비밀번호 확인
    if (passwordInput && confirmPasswordInput) {
        passwordInput.addEventListener('input', validatePasswordMatch);
        confirmPasswordInput.addEventListener('input', validatePasswordMatch);
    }

    // 이메일 변경 시 인증 리셋
    if (emailInput) {
        emailInput.addEventListener('input', resetEmailVerification);
    }

    // 닉네임 변경 시 중복확인 리셋
    if (nicknameInput) {
        nicknameInput.addEventListener('input', resetNicknameCheck);
    }

    // 모달 확인 버튼
    if (modalOkBtn) {
        modalOkBtn.addEventListener('click', () => {
            hideModal();
            window.location.href = '/login'; // 회원가입 후 로그인 페이지로 이동
        });
    }

    // ==============================
    //  회원가입 처리
    // ==============================
    function handleSignup(e) {
        e.preventDefault();

        if (!validateSignupForm()) return;

        const formData = {
            userId: userIdInput.value.trim(),
            email: emailInput.value.trim(),
            password: passwordInput.value.trim(),
            nickname: nicknameInput.value.trim()
        };

        showLoading(true);

        // TODO: 서버에 AJAX 요청
        console.log('회원가입 시도:', formData);

        // 임시: 2초 후 성공 처리
        setTimeout(() => {
            showLoading(false);
            showModal('회원가입 완료!', '성공적으로 가입되었습니다. 로그인 페이지로 이동해주세요.');
        }, 2000);
    }

    function validateSignupForm() {
        if (!userIdInput.value.trim()) {
            showToast('아이디를 입력해주세요.', 'error');
            return false;
        }
        if (!emailInput.value.trim()) {
            showToast('이메일을 입력해주세요.', 'error');
            return false;
        }
        if (!verificationState.emailVerified) {
            showToast('이메일 인증을 완료해주세요.', 'error');
            return false;
        }
        if (!passwordInput.value.trim()) {
            showToast('비밀번호를 입력해주세요.', 'error');
            return false;
        }
        if (passwordInput.value !== confirmPasswordInput.value) {
            showToast('비밀번호가 일치하지 않습니다.', 'error');
            return false;
        }
        if (!nicknameInput.value.trim()) {
            showToast('닉네임을 입력해주세요.', 'error');
            return false;
        }
        if (!verificationState.nicknameChecked) {
            showToast('닉네임 중복확인을 해주세요.', 'error');
            return false;
        }
        return true;
    }

    // ==============================
    //  이메일 인증
    // ==============================
    function handleSendEmailCode() {
        const email = emailInput.value.trim();
        if (!email) {
            showToast('이메일을 입력해주세요.', 'error');
            return;
        }
        console.log('인증번호 발송:', email);
        verificationState.emailSent = true;
        showElement(emailCodeGroup);
        showToast('인증번호가 발송되었습니다.', 'success');
    }

    function handleVerifyEmailCode() {
        if (emailCodeInput.value.trim() === '123456') { // 임시 코드
            verificationState.emailVerified = true;
            showElement(emailVerifiedMessage);
            showToast('이메일 인증이 완료되었습니다.', 'success');
        } else {
            showToast('인증번호가 일치하지 않습니다.', 'error');
        }
    }

    function resetEmailVerification() {
        verificationState.emailSent = false;
        verificationState.emailVerified = false;
        hideElement(emailCodeGroup);
        hideElement(emailVerifiedMessage);
        if (emailCodeInput) emailCodeInput.value = '';
    }

    // ==============================
    //  닉네임 확인
    // ==============================
    function handleCheckNickname() {
        const nickname = nicknameInput.value.trim();
        if (!nickname) {
            showToast('닉네임을 입력해주세요.', 'error');
            return;
        }
        console.log('닉네임 확인:', nickname);
        verificationState.nicknameChecked = true;
        showElement(nicknameCheckedMessage);
        showToast('사용 가능한 닉네임입니다.', 'success');
    }

    function resetNicknameCheck() {
        verificationState.nicknameChecked = false;
        hideElement(nicknameCheckedMessage);
    }

    // ==============================
    //  비밀번호 확인
    // ==============================
    function validatePasswordMatch() {
        if (passwordInput.value && confirmPasswordInput.value) {
            if (passwordInput.value === confirmPasswordInput.value) {
                passwordMatchMessage.textContent = '✓ 비밀번호가 일치합니다.';
                passwordMatchMessage.className = 'form-message success';
            } else {
                passwordMatchMessage.textContent = '✗ 비밀번호가 일치하지 않습니다.';
                passwordMatchMessage.className = 'form-message error';
            }
        } else {
            passwordMatchMessage.textContent = '';
            passwordMatchMessage.className = 'form-message';
        }
    }

    // ==============================
    //  UI 유틸
    // ==============================
    function showElement(el) {
        if (el) el.style.display = 'block';
    }
    function hideElement(el) {
        if (el) el.style.display = 'none';
    }
    function showLoading(show) {
        if (loadingOverlay) loadingOverlay.style.display = show ? 'flex' : 'none';
    }
    function showModal(title, message) {
        modalTitle.textContent = title;
        modalMessage.textContent = message;
        successModal.style.display = 'flex';
    }
    function hideModal() {
        successModal.style.display = 'none';
    }
    function showToast(message, type = 'info') {
        toastMessage.textContent = message;
        toast.className = `toast ${type}`;
        toast.style.display = 'block';
        setTimeout(() => toast.style.display = 'none', 3000);
    }
});
