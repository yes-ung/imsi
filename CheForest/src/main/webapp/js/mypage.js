// CheForest 마이페이지 JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // 탭 전환 기능 초기화
    initializeTabSwitching();

    // 탭 전환 기능 구현
    function initializeTabSwitching() {
        const menuItems = document.querySelectorAll('.menu-item');
        const tabContents = document.querySelectorAll('.tab-content');

        // 메뉴 항목 클릭 이벤트
        menuItems.forEach(item => {
            item.addEventListener('click', function() {
                const tabId = this.dataset.tab;

                // 모든 메뉴 항목에서 active 클래스 제거
                menuItems.forEach(menu => menu.classList.remove('active'));

                // 클릭한 메뉴 항목에 active 클래스 추가
                this.classList.add('active');

                // 모든 탭 컨텐츠 숨김
                tabContents.forEach(content => {
                    content.classList.remove('active');
                });

                // 선택한 탭 컨텐츠 표시 (애니메이션을 위한 약간의 지연)
                const targetTab = document.getElementById(`tab-${tabId}`);
                if (targetTab) {
                    setTimeout(() => {
                        targetTab.classList.add('active');
                    }, 50);
                }

                console.log('탭 전환:', tabId);
            });
        });
    }

    // 프로필 이미지 로딩 실패 시 폴백 처리
    initializeProfileImage();

    function initializeProfileImage() {
        const profileImage = document.getElementById('profile-image');
        const avatarFallback = document.getElementById('avatar-fallback');

        if (profileImage && avatarFallback) {
            profileImage.addEventListener('error', function() {
                profileImage.style.display = 'none';
                avatarFallback.style.display = 'flex';
                console.log('프로필 이미지 로딩 실패 - 폴백 표시');
            });

            profileImage.addEventListener('load', function() {
                profileImage.style.display = 'block';
                avatarFallback.style.display = 'none';
                console.log('프로필 이미지 로딩 성공');
            });
        }
    }

    // 새 레시피 작성 버튼
    const btnCreateRecipe = document.getElementById('btn-create-recipe');
    if (btnCreateRecipe) {
        btnCreateRecipe.addEventListener('click', function() {
            // 레시피 작성 페이지로 이동 로직
            console.log('새 레시피 작성 버튼 클릭');
        });
    }

    // 레시피 수정/삭제 버튼들
    document.addEventListener('click', function(e) {
        if (e.target.closest('.btn-edit')) {
            // 레시피 수정 로직
            console.log('레시피 수정 버튼 클릭');
        }

        if (e.target.closest('.btn-delete')) {
            // 삭제 확인 및 삭제 로직
            console.log('삭제 버튼 클릭');
        }

        if (e.target.closest('.btn-view') || e.target.closest('.btn-view-recipe')) {
            // 레시피 보기 로직
            console.log('레시피 보기 버튼 클릭');
        }
    });

    // 폼 제출 이벤트들 (구조만)

    // 프로필 업데이트 폼
    const profileForm = document.querySelector('#tab-settings form');
    if (profileForm) {
        profileForm.addEventListener('submit', function(e) {
            e.preventDefault();
            // 프로필 업데이트 로직
            console.log('프로필 업데이트 제출');
        });
    }

    // 비밀번호 변경 폼 검증
    const passwordInputs = {
        current: document.getElementById('current-password'),
        new: document.getElementById('new-password'),
        confirm: document.getElementById('confirm-password')
    };

    // 비밀번호 입력 필드 변경 이벤트 (구조만)
    Object.values(passwordInputs).forEach(input => {
        if (input) {
            input.addEventListener('input', function() {
                // 비밀번호 유효성 검사 로직
                console.log('비밀번호 입력 변경');
            });
        }
    });

    // 계정 삭제 버튼
    const btnDeleteAccount = document.querySelector('.btn-danger');
    if (btnDeleteAccount) {
        btnDeleteAccount.addEventListener('click', function() {
            // 계정 삭제 확인 다이얼로그 로직
            console.log('계정 삭제 버튼 클릭');
        });
    }

    // 통계 카드 호버 애니메이션 (구조만)
    const statCards = document.querySelectorAll('.stat-card');
    statCards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            // 호버 애니메이션 로직
            console.log('통계 카드 호버');
        });
    });

    // 반응형 사이드바 메뉴 스크롤 처리 (구조만)
    const menuList = document.querySelector('.menu-list');
    if (menuList && window.innerWidth <= 1024) {
        // 모바일에서 메뉴 스크롤 처리 로직
        console.log('모바일 메뉴 초기화');
    }

    // 창 크기 변경 이벤트
    window.addEventListener('resize', function() {
        // 반응형 처리 로직
        console.log('창 크기 변경');
    });

    // 페이지 로딩 완료 후 초기화
    initializePage();
});

// 페이지 초기화 함수
function initializePage() {
    // 기본 탭 활성화 (프로필 탭)
    showTab('profile');

    // 프로필 이미지 로딩 상태 확인
    checkProfileImage();

    // 등급 진행률 애니메이션 (구조만)
    animateProgressBar();

    console.log('마이페이지 초기화 완료');
}

// 탭 표시 함수 (구조만)
function showTab(tabId) {
    // 모든 탭 비활성화
    const tabContents = document.querySelectorAll('.tab-content');
    const menuItems = document.querySelectorAll('.menu-item');

    tabContents.forEach(tab => {
        tab.classList.remove('active');
    });

    menuItems.forEach(item => {
        item.classList.remove('active');
    });

    // 선택된 탭 활성화
    const selectedTab = document.getElementById(`tab-${tabId}`);
    const selectedMenu = document.querySelector(`[data-tab="${tabId}"]`);

    if (selectedTab) {
        selectedTab.classList.add('active');
    }

    if (selectedMenu) {
        selectedMenu.classList.add('active');
    }

    console.log('탭 전환:', tabId);
}

// 프로필 이미지 체크 함수
function checkProfileImage() {
    const profileImage = document.getElementById('profile-image');
    const avatarFallback = document.getElementById('avatar-fallback');

    if (profileImage && avatarFallback) {
        // 이미지 로딩 실패 시 폴백 표시 로직
        profileImage.style.display = 'block';
        avatarFallback.style.display = 'none';

        console.log('프로필 이미지 상태 확인');
    }
}

// 진행률 바 애니메이션 함수 (구조만)
function animateProgressBar() {
    const progressBars = document.querySelectorAll('.progress-fill');

    progressBars.forEach(bar => {
        // 진행률 애니메이션 로직
        console.log('진행률 바 애니메이션');
    });
}

// 폼 유효성 검사 함수들 (구조만)

// 닉네임 유효성 검사
function validateNickname(nickname) {
    // 닉네임 검증 로직
    console.log('닉네임 검증:', nickname);
    return true;
}

// 이메일 유효성 검사
function validateEmail(email) {
    // 이메일 검증 로직
    console.log('이메일 검증:', email);
    return true;
}

// 비밀번호 유효성 검사
function validatePassword(password) {
    // 비밀번호 검증 로직
    console.log('비밀번호 검증');
    return true;
}

// 비밀번호 일치 확인
function validatePasswordMatch(password, confirmPassword) {
    // 비밀번호 일치 검증 로직
    console.log('비밀번호 일치 확인');
    return password === confirmPassword;
}

// 알림 메시지 표시 함수 (구조만)
function showNotification(message, type = 'info') {
    // 알림 메시지 표시 로직
    console.log('알림:', message, type);
}

// 로딩 상태 표시 함수 (구조만)
function showLoading(show = true) {
    // 로딩 스피너 표시/숨김 로직
    console.log('로딩 상태:', show);
}

// 확인 다이얼로그 함수 (구조만)
function showConfirmDialog(message, callback) {
    // 확인 다이얼로그 표시 로직
    console.log('확인 다이얼로그:', message);
    // callback 실행 로직
}

// 데이터 업데이트 함수들 (구조만)

// 프로필 정보 업데이트
function updateProfile(data) {
    // 프로필 업데이트 API 호출 로직
    console.log('프로필 업데이트:', data);
}

// 비밀번호 변경
function changePassword(data) {
    // 비밀번호 변경 API 호출 로직
    console.log('비밀번호 변경');
}

// 레시피 삭제
function deleteRecipe(recipeId) {
    // 레시피 삭제 API 호출 로직
    console.log('레시피 삭제:', recipeId);
}

// 댓글 삭제
function deleteComment(commentId) {
    // 댓글 삭제 API 호출 로직
    console.log('댓글 삭제:', commentId);
}

// 계정 삭제
function deleteAccount() {
    // 계정 삭제 API 호출 로직
    console.log('계정 삭제');
}

// 유틸리티 함수들

// 날짜 포맷팅
function formatDate(date) {
    // 날짜 포맷팅 로직
    return date;
}

// 숫자 포맷팅 (천 단위 콤마)
function formatNumber(number) {
    // 숫자 포맷팅 로직
    return number.toLocaleString();
}

// 파일 크기 포맷팅
function formatFileSize(bytes) {
    // 파일 크기 포맷팅 로직
    return bytes + ' bytes';
}

// 등급 정보 가져오기
function getLevelInfo(level) {
    const levelInfo = {
        '씨앗': { minPosts: 0, color: 'green', icon: '🌱' },
        '뿌리': { minPosts: 1000, color: 'orange', icon: '🌿' },
        '새싹': { minPosts: 2000, color: 'blue', icon: '🌾' },
        '나무': { minPosts: 3000, color: 'purple', icon: '🌳' },
        '숲': { minPosts: 4000, color: 'pink', icon: '🌲' }
    };

    return levelInfo[level] || levelInfo['씨앗'];
}

// 다음 등급 계산
function getNextLevel(currentLevel) {
    const levels = ['씨앗', '뿌리', '새싹', '나무', '숲'];
    const currentIndex = levels.indexOf(currentLevel);
    return currentIndex < levels.length - 1 ? levels[currentIndex + 1] : null;
}

// 등급 진행률 계산
function calculateLevelProgress(currentPosts, currentLevel) {
    const levelInfo = getLevelInfo(currentLevel);
    const nextLevel = getNextLevel(currentLevel);

    if (!nextLevel) return 100; // 최고 등급

    const nextLevelInfo = getLevelInfo(nextLevel);
    const progress = ((currentPosts - levelInfo.minPosts) / (nextLevelInfo.minPosts - levelInfo.minPosts)) * 100;

    return Math.min(Math.max(progress, 0), 100);
}