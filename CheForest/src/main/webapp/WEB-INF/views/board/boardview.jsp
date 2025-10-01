<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="_csrf" content="${_csrf.token}"/>
  <meta name="_csrf_header" content="${_csrf.headerName}"/>
  <title><c:out value="${board.title}" default="게시글 상세보기"/> - CheForest</title>
  <link rel="stylesheet" href="/css/common/common.css">
  <link rel="stylesheet" href="/css/board/boardview.css">
  <script src="https://cdn.tailwindcss.com"></script>
  <script src="https://unpkg.com/lucide@latest/dist/umd/lucide.js"></script>
  <link href="https://fonts.googleapis.com/css2?family=Gowun+Dodum:wght@400&display=swap" rel="stylesheet">
  <style>
    /* 줄바꿈 보존용 */
    .whitespace-pre-line { white-space: pre-line; }
    /* 비어있을 때 안내 박스 */
    .empty-box { padding:16px; border:1px dashed #e5e7eb; border-radius:8px; color:#6b7280; background:#fafafa; }
  </style>
</head>
<body>
<jsp:include page="/common/header.jsp"/>

<div class="detail-container">

  <!-- 상단 네비게이션 (피그마 구조 + sticky 적용 위해 .top-nav 래퍼 추가) -->
  <div>
    <div class="nav-content">
      <button class="back-btn" id="backBtn" onclick="location.href='/board/list'">
        <svg class="back-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
          <path d="m15 18-6-6 6-6"/>
        </svg>
        <span>게시판으로</span>
      </button>

      <div class="nav-actions">
        <!-- 작성자 본인에게만 수정 버튼 노출 -->
        <sec:authorize access="isAuthenticated()">
          <button class="edit-btn" id="editBtn"
                  onclick="location.href='/board/edition?boardId=${board.boardId}'">
            <svg class="edit-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
              <path d="m18.5 2.5 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
            </svg>
            수정
          </button>
          <button class="delete-btn" id="deleteBtn" onclick="if(confirm('정말 삭제하시겠습니까?')) location.href='/board/delete?boardId=${board.boardId}'">
            <svg class="delete-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <path d="M3 6h18" />
              <path d="M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2" />
              <path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6" />
              <line x1="10" y1="11" x2="10" y2="17" />
              <line x1="14" y1="11" x2="14" y2="17" />
            </svg>
            삭제
          </button>
        </sec:authorize>
      </div>
    </div>
  </div>

  <c:choose>
  <c:when test="${board != null}">
  <div class="detail-content">

    <!-- 게시글 헤더 -->
    <div class="post-header">
      <div class="post-meta">
          <%-- 카테고리 클래스 산출 --%>
        <!-- 상태 배지 (카테고리) -->
        <c:set var="cat" value="${fn:trim(board.category)}" />
        <c:choose>
          <c:when test="${cat eq '한식'}"><c:set var="catClass" value="korean"/></c:when>
          <c:when test="${cat eq '양식'}"><c:set var="catClass" value="western"/></c:when>
          <c:when test="${cat eq '중식'}"><c:set var="catClass" value="chinese"/></c:when>
          <c:when test="${cat eq '일식'}"><c:set var="catClass" value="japanese"/></c:when>
          <c:when test="${cat eq '디저트'}"><c:set var="catClass" value="dessert"/></c:when>
          <c:otherwise><c:set var="catClass" value=""/></c:otherwise>
        </c:choose>

        <span class="category-badge ${catClass}">
                  <c:out value="${cat}" default="기타"/>
                </span>
        <h1 class="post-title">
          <c:out value="${board.title}" default="제목없음"/>
        </h1>
      </div>

      <!-- 작성자 정보 -->
      <div class="author-section">
        <div class="author-info">
          <div class="author-avatar">
            <img
                    src="<c:out value='${board.profile}' default='/images/default_profile.png'/>"
                    alt="작성자 프로필" class="avatar-img">
          </div>
          <div class="author-details">
            <div class="author-name-line">
              <span class="author-name"><c:out value="${board.nickname}" default="익명"/></span>
              <!-- 등급은 DTO에 없으므로 꾸밈용 뱃지 유지 -->
              <span class="user-grade grade-${board.grade}">
                    <c:out value="${board.grade}" />
                  </span>
            </div>
            <span class="post-date">
                ${board.insertTimeStr}
            </span>
          </div>
        </div>

        <!-- 액션 버튼들 -->
        <div class="action-buttons">
          <button class="action-btn like-btn" id="likeBtn" type="button">
            <svg class="heart-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <path d="M19.5 12.5 12 20l-7.5-7.5a5 5 0 0 1 7.5-7 5 5 0 0 1 7.5 7Z"/>
            </svg>
            <span class="count"><c:out value="${board.likeCount}" default="0"/></span>
          </button>

          <div class="action-btn view-btn" title="조회수">
            <svg class="eye-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/>
              <circle cx="12" cy="12" r="3"/>
            </svg>
            <span class="count"><c:out value="${board.viewCount}" default="0"/></span>
          </div>
        </div>
      </div>

      <!-- 게시글 기본 정보 -->
      <div class="post-info-grid">

        <!-- 조리시간 -->
        <div class="info-item flex flex-col items-center justify-center">
          <i data-lucide="clock" class="h-6 w-6 text-orange-500 mb-2"></i>
          <p class="info-label">조리시간</p>
          <p class="info-value">
            <c:out value="${board.cookTime}" default="-" />분
          </p>
        </div>

        <!-- 난이도 -->
        <div class="info-item flex flex-col items-center justify-center">
          <i data-lucide="chef-hat" class="h-6 w-6 text-orange-500 mb-2"></i>
          <p class="info-label">난이도</p>
          <span class="difficulty-badge">
                <c:out value="${board.difficulty}" default="-" />
              </span>
        </div>

      </div>
    </div>

    <!-- 메인 이미지 -->
    <div class="post-image">
      <img
              src="<c:out value='${thumbnailPath}' default='${board.thumbnail}'/>"
              alt="게시글 이미지"
              class="main-image"
              onerror="this.src='/images/default_thumbnail.png'">
    </div>

    <!-- 탭 콘텐츠 (재료/조리법) -->
    <div class="tabs-container">
      <!-- 탭 버튼들 -->
      <div class="tabs-list">
        <button class="tab-trigger active" id="ingredientsTab" data-tab="ingredients">재료</button>
        <button class="tab-trigger" id="instructionsTab" data-tab="instructions">조리법</button>
      </div>

      <!-- 재료 탭 내용 -->
      <div class="tab-content active" id="ingredientsContent">
        <div class="ingredients-card">
          <div class="ingredients-header">
            <h3 class="ingredients-title">필요한 재료</h3>
          </div>

          <!-- 줄바꿈 기반 단순 목록: fn:split 미사용, forTokens로 안전하게 -->
          <div class="ingredients-grid">
            <c:choose>
              <c:when test="${not empty board.prepare or not empty board.prepareAmount}">
                <c:set var="ingredients" value="${fn:split(board.prepare, ',')}" />
                <c:set var="amounts" value="${fn:split(board.prepareAmount, ',')}" />
                <c:forEach var="ing" items="${ingredients}" varStatus="status">
                  <div class="ingredient-item">
                    <span class="ingredient-name"><c:out value="${ing}" /></span>
                    <span class="ingredient-amount">
                          <c:out value="${amounts[status.index]}" default="-" />
                        </span>
                  </div>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <div class="empty-box">등록된 재료가 없습니다.</div>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </div>

      <!-- 조리법 탭 내용 -->
      <div class="tab-content" id="instructionsContent">
        <div class="instructions-list">
          <c:choose>
            <c:when test="${not empty instructions}">
              <c:forEach var="step" items="${instructions}" varStatus="status">
                <div class="instruction-item">
                  <div class="instruction-content">

                    <!-- ✅ 이미지 먼저 -->
                    <div class="instruction-image">
                      <c:choose>
                        <c:when test="${not empty step.image}">
                          <img src="<c:out value='${step.image}'/>"
                               class="step-image"
                               alt="조리 이미지"
                               onerror="this.src='/images/no-image.png'"/>
                        </c:when>
                        <c:otherwise>
                          <img src="/images/no-image.png" alt="No Image"/>
                        </c:otherwise>
                      </c:choose>
                    </div>

                    <!-- 그다음 텍스트 -->
                    <div class="instruction-text">
                      <p class="step-description">
                          ${status.index + 1}. <c:out value="${step.text}"/>
                      </p>
                    </div>

                  </div>
                </div>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <div class="empty-box">등록된 조리법이 없습니다.</div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>



      <!-- 댓글 영역 -->
      <div class="comments-section">
        <div class="comments-divider"></div>
        <div class="comments-header">
          <h3 class="comments-title">💬 댓글</h3>
        </div>

        <!-- 댓글 작성폼 -->
        <sec:authorize access="isAuthenticated()">
          <div class="comment-write">
            <div class="write-content">
              <div class="write-header">
                <div class="writer-avatar">
                  <img src="/images/default_profile.png" alt="내 프로필" class="avatar-img">
                </div>
                <div class="writer-info">
                  <div class="writer-name-line">
              <span class="writer-name">
                <sec:authentication property="principal.member.nickname"/>
              </span>
                    <span class="user-grade grade-${board.grade}">
                <c:out value="${board.grade}" />
              </span>
                  </div>
                </div>
              </div>
              <div class="write-form">
          <textarea id="commentTextarea" class="comment-textarea"
                    placeholder="이 게시글에 대한 생각을 나눠주세요! 👨‍🍳✨&#10;팁이나 의견도 환영해요!"></textarea>
                <button class="emoji-btn" id="emojiBtn" type="button" title="이모지">🙂</button>
              </div>
              <div class="write-footer">
                <div class="write-tip">💡 따뜻한 댓글로 소통의 즐거움을 나눠보세요</div>
                <button class="submit-btn" id="commentSubmitBtn" type="button">댓글 등록</button>
              </div>
            </div>
          </div>
        </sec:authorize>

        <!-- 비로그인 상태 -->
        <sec:authorize access="!isAuthenticated()">
          <div class="empty-box">✋ 댓글 작성은 로그인 후 이용해주세요</div>
        </sec:authorize>

        <!-- 댓글 리스트 -->
        <div class="comments-list" id="commentsList">
          <div class="empty-box">아직 댓글이 없습니다.</div>
        </div>

        <!-- ✅ 페이지네이션 (상위 댓글만) -->
        <div class="comments-pagination" id="commentsPager"></div>

        <div class="comment-guide">
          <div class="guide-content">
            <div class="guide-icon">💡</div>
            <div class="guide-text">
              <h4 class="guide-title">CheForest 댓글 가이드</h4>
              <p class="guide-description">
                • 요리 팁과 경험을 공유해주세요 • 서로 존중하는 따뜻한 소통을 해주세요 • 스팸/광고성 댓글은 삭제될 수 있습니다
              </p>
            </div>
          </div>
        </div>
      </div>

    </div>
    </c:when>

    <c:otherwise>
      <!-- board가 null일 때 -->
      <div class="detail-content">
        <div class="empty-box">존재하지 않거나 삭제된 게시글입니다.</div>
        <div style="margin-top:16px;">
          <button class="back-btn" onclick="location.href='/board/list'">목록으로</button>
        </div>
      </div>
    </c:otherwise>
    </c:choose>
  </div>

  <!-- ✅ 댓글 API용 전역변수 -->
  <sec:authorize access="isAuthenticated()">
  <script>
    window.boardId = <c:out value="${board.boardId}" default="0"/>;
    window.loginUser = {
      memberIdx: <sec:authentication property="principal.member.memberIdx" />,
      nickname: "<sec:authentication property="principal.member.nickname" />"
    };
  </script>
  </sec:authorize>

  <sec:authorize access="!isAuthenticated()">
  <script>
    window.boardId = <c:out value="${board.boardId}" default="0"/>;
    window.loginUser = { memberIdx: 0, nickname: "익명" };
  </script>
  </sec:authorize>
  <script src="/js/board/boardview.js"></script>
  <script src="/js/common/common.js"></script>
  <jsp:include page="/common/footer.jsp"/>

</body>
</html>
