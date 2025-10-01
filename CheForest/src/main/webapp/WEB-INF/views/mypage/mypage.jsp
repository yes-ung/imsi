<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>마이페이지 - CheForest</title>
  <link rel="stylesheet" href="/css/common/common.css">
  <link rel="stylesheet" href="/css/mypage.css">
</head>
<body>
<jsp:include page="/common/header.jsp"/>
<main class="mypage-main">
<%--  <!-- 1) 총 개수 -->--%>
<%--  <p>내가 쓴 글: <c:out value="${myPostsTotalCount}" /> 개</p>--%>

<%--  <!-- 2) 한 건만 테스트 -->--%>
<%--  <c:if test="${not empty myPosts}">--%>
<%--    <p>첫 번째 글 제목: <c:out value="${myPosts[0].title}" /></p>--%>
<%--    <p>작성일(원형): <c:out value="${myPosts[0].insertTime}" /></p>--%>
<%--  </c:if>--%>

<%--  <!-- 비어있는 경우 안내 -->--%>
<%--  <c:if test="${empty myPosts}">--%>
<%--    <p>작성한 글이 없습니다.</p>--%>
<%--  </c:if>--%>

  <!-- 페이지 헤더 -->
  <section class="mypage-header">
    <div class="container">
      <div class="profile-info">
        <div class="profile-avatar">
          <img src="사용자프로필이미지URL" alt="프로필 이미지" id="profile-image">
          <div class="avatar-fallback" id="avatar-fallback">사</div>
        </div>
        <div class="profile-details">
          <sec:authentication var="me" property="principal"/>
          <h1 class="profile-title"><c:out value="${me.member.nickname}"/>님의 마이페이지</h1>
          <div class="profile-meta">
            <span class="level-badge">
              <span class="level-icon">🌳</span>
              <span class="level-text">나무</span>
            </span>
            <span class="join-date">가입일: 2023년 3월 15일</span>
          </div>
          <div class="level-progress">
            <span class="progress-label">다음 등급까지</span>
            <div class="progress-bar">
              <div class="progress-fill" style="width: 75%"></div>
            </div>
            <span class="progress-text">75%</span>
          </div>
        </div>
      </div>
    </div>
  </section>



  <!-- 통계 섹션 -->
  <section class="stats-section">
    <div class="container">
      <div class="stats-grid">
        <%-- 작성한 레시피    --%>
        <div class="stat-card stat-recipes">
          <div class="stat-icon">👨‍🍳</div>
          <div class="stat-number">
            <fmt:formatNumber value="${empty myPostsTotalCount ? 0 : myPostsTotalCount}" />
          </div>
          <div class="stat-label">작성한 레시피</div>
        </div>
        <%--  작성한 댓글     --%>
        <div class="stat-card stat-comments">
          <div class="stat-icon">💬</div>
          <div class="stat-number">
            <fmt:formatNumber value="${empty myCommentsTotalCount ? 0 : myCommentsTotalCount}" />
          </div>
          <div class="stat-label">작성한 댓글</div>
        </div>

        <%--  받은 좋아요     --%>
        <div class="stat-card stat-likes">
          <div class="stat-icon">❤️</div>
          <div class="stat-number">
            <fmt:formatNumber value="${empty receivedLikesTotalCount ? 0 : receivedLikesTotalCount}" />
          </div>
          <div class="stat-label">받은 좋아요</div>
        </div>
          <div class="stat-card stat-views">
            <div class="stat-icon">🔍</div>
            <div class="stat-number"><fmt:formatNumber value="${myPostsTotalViewCount}" type="number"/></div>
            <div class="stat-label">총 조회수</div>
          </div>
      </div>
    </div>
  </section>

  <!-- 메인 컨텐츠 -->
  <section class="content-section">
    <div class="container">
      <div class="content-layout">
        <!-- 사이드바 -->
        <aside class="sidebar">
          <div class="sidebar-content">
            <h3 class="sidebar-title"><span class="sidebar-icon">👤</span>마이메뉴</h3>
            <div class="menu-list">
              <button class="menu-item active" data-tab="profile"><span class="menu-icon">👤</span><span class="menu-text">프로필</span></button>
              <button class="menu-item" data-tab="recipes"><span class="menu-icon">👨‍🍳</span><span class="menu-text">내 레시피</span></button>
              <button class="menu-item" data-tab="comments"><span class="menu-icon">💬</span><span class="menu-text">내 댓글</span></button>
              <button class="menu-item" data-tab="liked"><span class="menu-icon">❤️</span><span class="menu-text">좋아요</span></button>
              <button class="menu-item" data-tab="settings"><span class="menu-icon">⚙️</span><span class="menu-text">설정</span></button>
            </div>
          </div>
        </aside>

        <!-- 메인 컨텐츠 본문(※ main 금지, section/div 사용) -->
        <section class="main-content">

          <!-- 프로필 탭 -->
          <div class="tab-content active" id="tab-profile">
            <div class="content-grid">
              <!-- 등급 현황 카드 -->
              <div class="info-card">
                <div class="card-header">
                  <h3 class="card-title"><span class="title-icon">🏆</span>등급 현황</h3>
                </div>
                <div class="card-content">
                  <div class="level-status">
                    <div class="current-level">
                      <h4>현재 등급</h4>
                      <div class="level-display level-tree">
                        <div class="level-emoji">🌳</div>
                        <div class="level-name">나무</div>
                        <div class="level-desc">레시피 24개 작성</div>
                      </div>
                    </div>
                    <div class="next-level">
                      <h4>다음 등급</h4>
                      <div class="level-display level-forest">
                        <div class="level-emoji">🌲</div>
                        <div class="level-name">숲</div>
                        <div class="level-desc">레시피 26개 더 필요</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 활동 현황 카드 -->
              <div class="info-card">
                <div class="card-header">
                  <h3 class="card-title"><span class="title-icon">📈</span>활동 현황</h3>
                </div>
                <div class="card-content">
                  <div class="activity-list">
                    <div class="activity-item"><span class="activity-label">이번 달 레시피 작성</span><span class="activity-badge">3개</span></div>
                    <div class="activity-item"><span class="activity-label">이번 달 댓글 작성</span><span class="activity-badge">15개</span></div>
                    <div class="activity-item"><span class="activity-label">평균 레시피 조회수</span><span class="activity-badge">518회</span></div>
                    <div class="activity-item"><span class="activity-label">평균 좋아요 수</span><span class="activity-badge">77개</span></div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 내 레시피 탭 -->
          <div class="tab-content" id="tab-recipes">
            <div class="tab-header">
              <h2 class="tab-title"><c:choose>
                <c:when test="${activeTab eq 'myboard'}">
                  내 레시피 (<c:out value='${myPostsTotalCount}' default='0'/>개)
                </c:when>
                <c:when test="${activeTab eq 'liked-recipe'}">
                  좋아요한 레시피 (<c:out value='${likedRecipesTotalCount}' default='0'/>개)
                </c:when>
                <c:otherwise>
                  좋아요한 게시글 (<c:out value='${likedPostsTotalCount}' default='0'/>개)
                </c:otherwise>
              </c:choose></h2>
              <a href="<c:out value='/board/add'/>"
                 class="btn-primary" id="btn-create-recipe">
                <span class="btn-icon">👨‍🍳</span>새 레시피 작성</a>
            </div>
            <div class="recipe-list">
              <div class="recipe-item">
                <img src="레시피이미지URL" alt="레시피 제목" class="recipe-image">
                <div class="recipe-info">
                  <h3 class="recipe-title">레시피 제목 자리</h3>
                  <div class="recipe-meta">
                    <span class="recipe-category">카테고리명</span>
                    <span class="recipe-stat"><span class="stat-icon">👁️</span><span>조회수</span></span>
                    <span class="recipe-stat"><span class="stat-icon">❤️</span><span>좋아요수</span></span>
                    <span class="recipe-date">작성일</span>
                  </div>
                </div>
                <div class="recipe-actions">
                  <button class="btn-edit"><span class="btn-icon">✏️</span>수정</button>
                  <button class="btn-delete"><span class="btn-icon">🗑️</span>삭제</button>
                </div>
              </div>
            </div>
          </div>


          <!-- 내 댓글 탭 -->
          <div class="tab-content" id="tab-comments">
            <h2 class="tab-title">작성한 댓글 (156개)</h2>
            <div class="comment-list">
              <div class="comment-item">
                <div class="comment-header">
                  <h3 class="comment-recipe-title">레시피 제목 자리</h3>
                  <span class="comment-date">작성일</span>
                </div>
                <p class="comment-content">댓글 내용 자리입니다. 실제 댓글 내용이 여기에 표시됩니다.</p>
                <div class="comment-actions">
                  <button class="btn-view">레시피 보기</button>
                  <button class="btn-delete"><span class="btn-icon">🗑️</span>삭제</button>
                </div>
              </div>
            </div>
          </div>

          <!-- 좋아요 탭 -->
          <div class="tab-content" id="tab-liked">
            <h2 class="tab-title">좋아요한 레시피 (47개)</h2>
            <div class="liked-list">
              <div class="liked-item">
                <img src="레시피이미지URL" alt="레시피 제목" class="liked-image">
                <div class="liked-info">
                  <h3 class="liked-title">레시피 제목 자리</h3>
                  <div class="liked-meta">
                    <span class="liked-author">by 작성자닉네임</span>
                    <span class="liked-category">카테고리명</span>
                    <span class="liked-date">작성일</span>
                  </div>
                </div>
                <button class="btn-view-recipe">레시피 보기<span class="btn-arrow">→</span></button>
              </div>
            </div>
          </div>

          <!-- 설정 탭 -->
          <div class="tab-content" id="tab-settings">
            <div class="settings-grid">
              <!-- 계정 정보 카드 -->
              <div class="settings-card">
                <div class="card-header">
                  <h3 class="card-title"><span class="title-icon">👤</span>계정 정보</h3>
                </div>
                <div class="card-content">
                  <div class="form-grid">
                    <div class="form-group">
                      <label for="nickname">닉네임</label>
                      <input type="text" id="nickname" value="사용자닉네임">
                    </div>
                    <div class="form-group">
                      <label for="email">이메일</label>
                      <input type="email" id="email" value="user@example.com">
                    </div>
                  </div>
                  <button class="btn-primary">프로필 업데이트</button>
                </div>
              </div>

              <!-- 비밀번호 변경 카드 -->
              <div class="settings-card">
                <div class="card-header">
                  <h3 class="card-title"><span class="title-icon">🔒</span>비밀번호 변경</h3>
                </div>
                <div class="card-content">
                  <div class="form-group">
                    <label for="current-password">현재 비밀번호</label>
                    <input type="password" id="current-password">
                  </div>
                  <div class="form-group">
                    <label for="new-password">새 비밀번호</label>
                    <input type="password" id="new-password">
                  </div>
                  <div class="form-group">
                    <label for="confirm-password">새 비밀번호 확인</label>
                    <input type="password" id="confirm-password">
                  </div>
                  <button class="btn-primary">비밀번호 변경</button>
                </div>
              </div>

              <!-- 계정 관리 카드 -->
              <div class="settings-card danger-card">
                <div class="card-header">
                  <h3 class="card-title"><span class="title-icon">🛡️</span>계정 관리</h3>
                </div>
                <div class="card-content">
                  <div class="danger-zone">
                    <h4 class="danger-title">계정 삭제</h4>
                    <p class="danger-text">계정을 삭제하면 모든 데이터가 영구적으로 삭제되며 복구할 수 없습니다.</p>
                    <button class="btn-danger">계정 삭제하기</button>
                  </div>
                </div>
              </div>
            </div>
          </div>

        </section> <!-- /.main-content -->
      </div> <!-- /.content-layout -->
    </div> <!-- /.container -->
  </section> <!-- /.content-section -->
</main> <!-- /.mypage-main -->

<!-- 푸터는 제외 -->
<jsp:include page="/common/footer.jsp"/>

<script src="/js/common.js"></script>
<script src="/js/mypage.js"></script>
</body>
</html>