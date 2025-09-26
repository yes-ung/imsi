// CheForest 게시글 상세보기 JavaScript
// 주의: 이 파일은 기본 구조만 제공합니다. 실제 동작은 JSP 개발 시 구현해주세요.

document.addEventListener('DOMContentLoaded', function() {

    // 뒤로가기 버튼
    const backBtn = document.getElementById('backBtn');
    if (backBtn) {
        backBtn.addEventListener('click', function() {
            // TODO: JSP에서 게시판 목록 페이지로 이동 구현
            console.log('게시판 목록으로 이동');
        });
    }

    // 수정 버튼
    const editBtn = document.getElementById('editBtn');
    if (editBtn) {
        editBtn.addEventListener('click', function() {
            // TODO: JSP에서 게시글 수정 페이지로 이동 구현
            console.log('게시글 수정 페이지로 이동');
        });
    }

    // 좋아요 버튼
    const likeBtn = document.getElementById('likeBtn');
    if (likeBtn) {
        likeBtn.addEventListener('click', function() {
            // TODO: JSP에서 좋아요 토글 구현
            this.classList.toggle('liked');
            console.log('좋아요 토글');
        });
    }

    // 북마크 버튼
    const bookmarkBtn = document.getElementById('bookmarkBtn');
    if (bookmarkBtn) {
        bookmarkBtn.addEventListener('click', function() {
            // TODO: JSP에서 북마크 토글 구현
            this.classList.toggle('bookmarked');
            console.log('북마크 토글');
        });
    }

    // 이모지 버튼
    const emojiBtn = document.getElementById('emojiBtn');
    if (emojiBtn) {
        emojiBtn.addEventListener('click', function() {
            // TODO: JSP에서 이모지 선택 모달 구현
            console.log('이모지 선택');
        });
    }

    // 댓글 등록 버튼
    const commentSubmitBtn = document.getElementById('commentSubmitBtn');
    const commentTextarea = document.getElementById('commentTextarea');

    if (commentSubmitBtn && commentTextarea) {
        // 댓글 입력 시 버튼 활성화/비활성화
        commentTextarea.addEventListener('input', function() {
            if (this.value.trim().length > 0) {
                commentSubmitBtn.disabled = false;
            } else {
                commentSubmitBtn.disabled = true;
            }
        });

        // 댓글 등록
        commentSubmitBtn.addEventListener('click', function() {
            const content = commentTextarea.value.trim();
            if (content) {
                // TODO: JSP에서 댓글 등록 AJAX 구현
                console.log('댓글 등록:', content);
                commentTextarea.value = '';
                this.disabled = true;
            }
        });
    }

    // 댓글 메뉴 버튼
    const commentMenuBtn = document.getElementById('commentMenuBtn');
    if (commentMenuBtn) {
        commentMenuBtn.addEventListener('click', function() {
            // TODO: JSP에서 댓글 메뉴 드롭다운 구현
            console.log('댓글 메뉴 표시');
        });
    }

    // 댓글 좋아요 버튼
    const commentLikeBtn = document.getElementById('commentLikeBtn');
    if (commentLikeBtn) {
        commentLikeBtn.addEventListener('click', function() {
            // TODO: JSP에서 댓글 좋아요 토글 구현
            this.classList.toggle('liked');
            console.log('댓글 좋아요 토글');
        });
    }

    // 댓글 싫어요 버튼
    const commentDislikeBtn = document.getElementById('commentDislikeBtn');
    if (commentDislikeBtn) {
        commentDislikeBtn.addEventListener('click', function() {
            // TODO: JSP에서 댓글 싫어요 토글 구현
            this.classList.toggle('disliked');
            console.log('댓글 싫어요 토글');
        });
    }

    // 답글 버튼
    const replyBtn = document.getElementById('replyBtn');
    const replyWrite = document.getElementById('replyWrite');

    if (replyBtn && replyWrite) {
        replyBtn.addEventListener('click', function() {
            // 답글 작성 영역 토글
            if (replyWrite.style.display === 'none' || !replyWrite.style.display) {
                replyWrite.style.display = 'block';
            } else {
                replyWrite.style.display = 'none';
            }
        });
    }

    // 답글 취소 버튼
    const replyCancelBtn = document.getElementById('replyCancelBtn');
    const replyTextarea = document.getElementById('replyTextarea');

    if (replyCancelBtn && replyWrite && replyTextarea) {
        replyCancelBtn.addEventListener('click', function() {
            replyWrite.style.display = 'none';
            replyTextarea.value = '';
        });
    }

    // 답글 등록 버튼
    const replySubmitBtn = document.getElementById('replySubmitBtn');

    if (replySubmitBtn && replyTextarea) {
        // 답글 입력 시 버튼 활성화/비활성화
        replyTextarea.addEventListener('input', function() {
            if (this.value.trim().length > 0) {
                replySubmitBtn.disabled = false;
            } else {
                replySubmitBtn.disabled = true;
            }
        });

        // 답글 등록
        replySubmitBtn.addEventListener('click', function() {
            const content = replyTextarea.value.trim();
            if (content) {
                // TODO: JSP에서 답글 등록 AJAX 구현
                console.log('답글 등록:', content);
                replyTextarea.value = '';
                replyWrite.style.display = 'none';
                this.disabled = true;
            }
        });
    }

    // 더 많은 댓글 보기 버튼
    const loadMoreBtn = document.getElementById('loadMoreBtn');
    if (loadMoreBtn) {
        loadMoreBtn.addEventListener('click', function() {
            // TODO: JSP에서 추가 댓글 로드 구현
            console.log('더 많은 댓글 로드');
        });
    }

    // 텍스트 영역 자동 높이 조절
    function autoResizeTextarea(textarea) {
        textarea.style.height = 'auto';
        textarea.style.height = textarea.scrollHeight + 'px';
    }

    // 모든 텍스트 영역에 자동 높이 조절 적용
    const textareas = document.querySelectorAll('textarea');
    textareas.forEach(textarea => {
        textarea.addEventListener('input', function() {
            autoResizeTextarea(this);
        });
    });

    // 초기 버튼 상태 설정
    if (commentSubmitBtn) {
        commentSubmitBtn.disabled = true;
    }

    if (replySubmitBtn) {
        replySubmitBtn.disabled = true;
    }

    // 탭 전환 기능
    const tabTriggers = document.querySelectorAll('.tab-trigger');
    const tabContents = document.querySelectorAll('.tab-content');

    tabTriggers.forEach(trigger => {
        trigger.addEventListener('click', function() {
            const targetTab = this.getAttribute('data-tab');

            // 모든 탭 버튼 비활성화
            tabTriggers.forEach(t => t.classList.remove('active'));
            // 모든 탭 콘텐츠 숨기기
            tabContents.forEach(c => c.classList.remove('active'));

            // 클릭된 탭 버튼 활성화
            this.classList.add('active');
            // 해당 탭 콘텐츠 표시
            document.getElementById(targetTab + 'Content').classList.add('active');
        });
    });

    // 스크롤 시 상단 네비게이션 그림자 효과
    const topNav = document.querySelector('.top-nav');

    if (topNav) {
        window.addEventListener('scroll', function() {
            if (window.scrollY > 100) {
                topNav.style.boxShadow = '0 2px 4px rgba(0, 0, 0, 0.1)';
            } else {
                topNav.style.boxShadow = 'none';
            }
        });
    }

    // 이미지 로드 에러 처리
    const images = document.querySelectorAll('img');
    images.forEach(img => {
        img.addEventListener('error', function() {
            this.src = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjI0IiBoZWlnaHQ9IjI0IiBmaWxsPSIjRjNGNEY2Ii8+CjxwYXRoIGQ9Ik0xMiAxNkMxNC4yMDkxIDEgMTYgMTQuMjA5MSAxNiAxMkMxNiA5Ljc5MDg2IDE0LjIwOTEgOCAxMiA4QzkuNzkwODYgOCA4IDkuNzkwODYgOCAxMkM4IDE0LjIwOTEgOS43OTA4NiAxNiAxMiAxNloiIGZpbGw9IiM5Q0EzQUYiLz4KPC9zdmc+';
            this.alt = '이미지를 불러올 수 없습니다';
        });
    });

    console.log('게시글 상세보기 페이지 초기화 완료');
});

// 공통 유틸리티 함수들

// 날짜 포맷팅 함수
function formatDate(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = Math.abs(now - date);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    if (diffDays === 1) {
        return '오늘';
    } else if (diffDays === 2) {
        return '어제';
    } else if (diffDays <= 7) {
        return `${diffDays - 1}일 전`;
    } else {
        return date.toLocaleDateString('ko-KR');
    }
}

// 숫자 포맷팅 함수
function formatNumber(num) {
    if (num >= 1000000) {
        return (num / 1000000).toFixed(1) + 'M';
    } else if (num >= 1000) {
        return (num / 1000).toFixed(1) + 'K';
    }
    return num.toString();
}

// 텍스트 길이 제한 함수
function limitText(text, maxLength) {
    if (text.length <= maxLength) {
        return text;
    }
    return text.substring(0, maxLength) + '...';
}

// 쿠키 설정 함수
function setCookie(name, value, days) {
    const expires = new Date();
    expires.setTime(expires.getTime() + (days * 24 * 60 * 60 * 1000));
    document.cookie = name + '=' + value + ';expires=' + expires.toUTCString() + ';path=/';
}

// 쿠키 가져오기 함수
function getCookie(name) {
    const nameEQ = name + '=';
    const ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}