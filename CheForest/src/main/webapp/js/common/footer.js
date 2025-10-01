/* CheForest Footer JavaScript */
/* 푸터 컴포넌트 전용 JavaScript */

// 뉴스레터 구독 처리
function handleNewsletterSubscription() {
    const emailInput = document.querySelector('.newsletter-email');
    const subscribeBtn = document.querySelector('.newsletter-subscribe-btn');
    
    if (emailInput && subscribeBtn) {
        const email = emailInput.value.trim();
        
        // 이메일 유효성 검사
        if (!isValidEmail(email)) {
            showNotification('올바른 이메일 주소를 입력해주세요.', 'error');
            return;
        }
        
        // 구독 처리 (실제로는 서버로 전송)
        subscribeBtn.textContent = '구독 중...';
        subscribeBtn.disabled = true;
        
        // 시뮬레이션: 2초 후 완료
        setTimeout(() => {
            showNotification('뉴스레터 구독이 완료되었습니다!', 'success');
            emailInput.value = '';
            subscribeBtn.textContent = '구독하기';
            subscribeBtn.disabled = false;
        }, 2000);
    }
}

// 이메일 유효성 검사
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// 알림 표시 함수
function showNotification(message, type = 'info') {
    // 기존 알림 제거
    const existingNotification = document.querySelector('.notification');
    if (existingNotification) {
        existingNotification.remove();
    }
    
    // 새 알림 생성
    const notification = document.createElement('div');
    notification.className = `notification fixed top-4 right-4 px-6 py-3 rounded-lg shadow-lg z-50 transition-all duration-300 ${getNotificationStyle(type)}`;
    notification.textContent = message;
    
    document.body.appendChild(notification);
    
    // 3초 후 자동 제거
    setTimeout(() => {
        notification.style.opacity = '0';
        notification.style.transform = 'translateX(100%)';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, 3000);
}

// 알림 스타일 반환
function getNotificationStyle(type) {
    switch (type) {
        case 'success':
            return 'bg-green-500 text-white';
        case 'error':
            return 'bg-red-500 text-white';
        case 'warning':
            return 'bg-yellow-500 text-white';
        default:
            return 'bg-blue-500 text-white';
    }
}

// 소셜 미디어 링크 처리
function handleSocialClick(platform) {
    const socialLinks = {
        facebook: 'https://facebook.com/cheforest',
        twitter: 'https://twitter.com/cheforest',
        instagram: 'https://instagram.com/cheforest',
        youtube: 'https://youtube.com/cheforest'
    };
    
    if (socialLinks[platform]) {
        window.open(socialLinks[platform], '_blank');
    }
}

// 앱 다운로드 링크 처리
function handleAppDownload(platform) {
    const downloadLinks = {
        ios: 'https://apps.apple.com/app/cheforest',
        android: 'https://play.google.com/store/apps/details?id=com.cheforest'
    };
    
    if (downloadLinks[platform]) {
        window.open(downloadLinks[platform], '_blank');
    }
}

// QR 코드 호버 효과
function setupQRCodeEffect() {
    const qrCode = document.querySelector('.qr-code');
    if (qrCode) {
        qrCode.addEventListener('mouseenter', function() {
            this.style.transform = 'scale(1.1)';
        });
        
        qrCode.addEventListener('mouseleave', function() {
            this.style.transform = 'scale(1)';
        });
    }
}

// 푸터 링크 애니메이션 설정
function setupFooterLinkAnimations() {
    const footerLinks = document.querySelectorAll('.footer-link');
    footerLinks.forEach(link => {
        link.addEventListener('mouseenter', function() {
            this.style.transform = 'translateX(4px)';
        });
        
        link.addEventListener('mouseleave', function() {
            this.style.transform = 'translateX(0)';
        });
    });
}

// 뉴스레터 입력 이벤트 설정
function setupNewsletterEvents() {
    const emailInput = document.querySelector('.newsletter-email');
    const subscribeBtn = document.querySelector('.newsletter-subscribe-btn');
    
    if (emailInput && subscribeBtn) {
        // Enter 키 이벤트
        emailInput.addEventListener('keydown', function(event) {
            if (event.key === 'Enter') {
                handleNewsletterSubscription();
            }
        });
        
        // 구독 버튼 클릭 이벤트
        subscribeBtn.addEventListener('click', handleNewsletterSubscription);
        
        // 실시간 이메일 유효성 검사
        emailInput.addEventListener('input', function() {
            const email = this.value.trim();
            if (email && !isValidEmail(email)) {
                this.style.borderColor = '#ef4444';
            } else {
                this.style.borderColor = '';
            }
        });
    }
}

// 푸터 통계 애니메이션
function animateFooterStats() {
    const statCards = document.querySelectorAll('.footer-stat-card');
    
    // Intersection Observer로 스크롤 시 애니메이션
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.animation = 'slideInUp 0.6s ease-out';
            }
        });
    });
    
    statCards.forEach(card => {
        observer.observe(card);
    });
}

// 푸터 초기화
function initializeFooter() {
    setupNewsletterEvents();
    setupQRCodeEffect();
    setupFooterLinkAnimations();
    animateFooterStats();
}

// 페이지 로드 시 푸터 초기화
document.addEventListener('DOMContentLoaded', function() {
    initializeFooter();
});

// 푸터 함수들을 전역 객체에 등록
window.CheForest = window.CheForest || {};
window.CheForest.footer = {
    handleNewsletterSubscription,
    handleSocialClick,
    handleAppDownload,
    showNotification,
    initializeFooter
};