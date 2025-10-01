<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="/css/common/common.css">
    <link rel="stylesheet" href="/css/events.css">
</head>
<body>
    <!-- 쿠킹 스튜디오 섹션 -->
    <section class="events-section">
        <!-- 섹션 헤더 -->
        <div class="events-header">
            <div class="header-title-container">
                <svg class="trophy-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                    <path d="M6 9H4.5a2.5 2.5 0 0 1 0-5H6"/>
                    <path d="M18 9h1.5a2.5 2.5 0 0 0 0-5H18"/>
                    <path d="M4 22h16"/>
                    <path d="M10 14.66V17c0 .55-.47.98-.97 1.21C7.85 18.75 7 20.24 7 22"/>
                    <path d="M14 14.66V17c0 .55.47.98.97 1.21C16.15 18.75 17 20.24 17 22"/>
                    <path d="M18 2H6v7a6 6 0 0 0 12 0V2Z"/>
                </svg>
                <h2 class="gradient-title">쿠킹 스튜디오</h2>
            </div>
            <p class="events-description">
                CheForest의 쿠킹 스튜디오에서 새로운 요리 경험을 만나보세요! 
                창의적인 레시피 공모전부터 나만의 요리 성향 발견까지, 당신의 요리 여정을 함께 시작해보세요.
            </p>
        </div>

        <!-- 이벤트 카드 그리드 -->
        <div class="events-grid">
            <!-- 레시피 공모전 카드 -->
            <div class="event-card" data-event-id="1">
                <div class="card-image-container">
                    <img src="https://images.unsplash.com/photo-1741980983785-d879e5c4f50c?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxyZWNpcGUlMjBjb250ZXN0JTIwYXdhcmQlMjBwcml6ZXxlbnwxfHx8fDE3NTc1NzgyNzl8MA&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral" 
                        alt="2025 CheForest 레시피 공모전" 
                        class="card-image">
                    <div class="card-badges">
                        <span class="badge badge-status badge-active">모집중</span>
                        <span class="badge badge-category">공모전</span>
                    </div>
                </div>

                <div class="card-header">
                    <h3 class="card-title">2025 CheForest 레시피 공모전</h3>
                    <p class="card-description">
                        창의적이고 건강한 레시피로 도전하세요! 우승자에게는 100만원 상금과 CheForest 명예의 전당에 이름이 등록됩니다.
                    </p>
                </div>

                <div class="card-content">
                    <!-- 이벤트 정보 -->
                    <div class="event-info-grid">
                        <div class="info-item">
                            <svg class="info-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                                <line x1="16" y1="2" x2="16" y2="6"/>
                                <line x1="8" y1="2" x2="8" y2="6"/>
                                <line x1="3" y1="10" x2="21" y2="10"/>
                            </svg>
                            <span>2025-04-22 ~ 2025-10-21</span>
                        </div>
                        <div class="info-item">
                            <svg class="info-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <circle cx="12" cy="12" r="10"/>
                                <polyline points="12,6 12,12 16,14"/>
                            </svg>
                            <span>24시간 접수</span>
                        </div>
                        <div class="info-item">
                            <svg class="info-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/>
                                <circle cx="9" cy="7" r="4"/>
                                <path d="M22 21v-2a4 4 0 0 0-3-3.87"/>
                                <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
                            </svg>
                            <span>1,247명 참여</span>
                        </div>
                        <div class="info-item">
                            <svg class="info-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path d="M6 13.87A4 4 0 0 1 7.41 6a5.11 5.11 0 0 1 1.05-1.54 5 5 0 0 1 7.08 0A5.11 5.11 0 0 1 16.59 6 4 4 0 0 1 18 13.87V21H6Z"/>
                                <line x1="6" y1="17" x2="18" y2="17"/>
                                <line x1="12" y1="17" x2="12" y2="21"/>
                            </svg>
                            <span>총 상금 300만원</span>
                        </div>
                    </div>

                    <!-- 액션 버튼 -->
                    <div class="card-actions">
                        <button class="btn btn-primary" onclick="participateEvent(1)">지금 참여하기</button>
                        <button class="btn btn-outline" onclick="viewEventDetail(1)">자세히 보기</button>
                    </div>
                </div>
            </div>

            <!-- 레시피 성향 테스트 카드 -->
            <div class="event-card" data-event-id="2">
                <div class="card-image-container test-card-bg">
                    <img src="https://images.unsplash.com/photo-1705325479405-6147a560e628?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHxjdXRlJTIwYmVhciUyMGNvb2tpbmclMjBjaGVmfGVufDF8fHx8MTc1ODA5NjMyMXww&ixlib=rb-4.1.0&q=80&w=1080&utm_source=figma&utm_medium=referral" 
                         alt="요리 테스트 캐릭터" 
                         class="test-character">
                </div>

                <div class="card-header">
                    <h3 class="card-title test-title gradient-title">나랑 어울리는 요리는?</h3>
                    <p class="card-description test-description">
                        나와 어울리는 요리를 찾아보세요! 간단한 질문으로 당신만의 요리 스타일을 발견해보세요
                    </p>
                </div>

                <div class="card-content">
                    <!-- 액션 버튼 -->
                    <div class="card-actions">
                        <button class="btn btn-primary" onclick="showPage('test')">
                            <svg class="btn-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                <path d="M9 12l2 2 4-4"/>
                                <path d="M21 12c.552 0 1-.448 1-1s-.448-1-1-1-1 .448-1 1 .448 1 1 1z"/>
                                <path d="M19 12c0-7-7-7-7-7s-7 0-7 7c0 1.5.5 2.5 1.5 3.5L12 21l5.5-5.5c1-1 1.5-2 1.5-3.5z"/>
                            </svg>
                            테스트 시작
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- 더 많은 이벤트 버튼 -->
        <div class="more-events-section">
            <button class="btn btn-gradient" onclick="viewMoreEvents()">더 많은 이벤트 보기</button>
        </div>
    </section>

    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- Lucide Icons -->
    <script src="https://unpkg.com/lucide@latest/dist/umd/lucide.js"></script>
    <script src="/js/common/common.js"></script>
    <script src="/js/events.js"></script>
</body>
</html>