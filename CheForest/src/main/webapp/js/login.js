document.addEventListener('DOMContentLoaded', function() {
    const backBtn = document.getElementById('backBtn');
    const loginForm = document.getElementById('loginForm');
    const submitBtn = document.getElementById('submitBtn');
    const submitBtnText = document.getElementById('submitBtnText');
    const cardDescription = document.getElementById('cardDescription');
    const loginLinks = document.getElementById('loginLinks');

    const userIdInput = document.getElementById('userId');
    const passwordInput = document.getElementById('password');

    const successModal = document.getElementById('successModal');
    const modalTitle = document.getElementById('modalTitle');
    const modalMessage = document.getElementById('modalMessage');
    const modalOkBtn = document.getElementById('modalOkBtn');
    const loadingOverlay = document.getElementById('loadingOverlay');
    const toast = document.getElementById('toast');
    const toastMessage = document.getElementById('toastMessage');

    // 뒤로가기 버튼
    if (backBtn) {
        backBtn.addEventListener('click', function() {
            window.location.href = '/';
        });
    }

    // 폼 제출 (로그인만)
    if (loginForm) {
        loginForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            const userId = userIdInput.value.trim();
            const password = passwordInput.value.trim();

            if (!userId || !password) {
                showToast('아이디와 비밀번호를 입력해주세요.', 'error');
                return;
            }

            showLoading(true);

            try {
                const res = await fetch("/auth/login", {
                    method: "POST",
                    body: new URLSearchParams({ loginId: userId, password: password }),
                });

                showLoading(false);

                if (res.ok) {
                    showModal('로그인 성공!', 'CheForest에 오신 것을 환영합니다!');
                    modalOkBtn.onclick = () => { window.location.href = '/'; };
                } else {
                    showToast('아이디 또는 비밀번호가 잘못되었습니다.', 'error');
                }
            } catch (err) {
                showLoading(false);
                console.error("요청 실패:", err);
                showToast('서버 오류가 발생했습니다.', 'error');
            }
        });
    }

    // 로딩
    function showLoading(show) {
        if (loadingOverlay) {
            loadingOverlay.style.display = show ? 'flex' : 'none';
        }
    }

    // 모달
    function showModal(title, message) {
        if (modalTitle) modalTitle.textContent = title;
        if (modalMessage) modalMessage.textContent = message;
        if (successModal) successModal.style.display = 'flex';
    }

    // 토스트
    function showToast(message, type = 'info') {
        if (!toast || !toastMessage) return;
        toastMessage.textContent = message;
        toast.className = `toast ${type}`;
        toast.style.display = 'block';
        setTimeout(() => { toast.style.display = 'none'; }, 3000);
    }

    console.log('로그인 페이지 초기화 완료');
});
