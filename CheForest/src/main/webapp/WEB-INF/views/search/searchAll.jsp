<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>통합검색 - CheForest</title>
  <link rel="stylesheet" href="css/common.css">
  <link rel="stylesheet" href="css/search.css">
  <!-- Tailwind CSS -->
  <script src="https://cdn.tailwindcss.com"></script>
  <!-- Lucide Icons -->
  <script src="https://unpkg.com/lucide@latest/dist/umd/lucide.js"></script>
</head>
<body>
<jsp:include page="/common/header.jsp"/>
<!-- 헤더는 제외 -->

<main class="search-main">
  <!-- 검색 헤더 -->
  <section class="search-header">
    <div class="container">
      <div class="header-content">
        <div class="header-title">
          <span class="search-icon">🔍</span>
          <h1>CheForest 통합 검색</h1>
        </div>
        <p class="header-description">
          레시피, 게시글, Q&A를 한 번에 검색하세요
        </p>
      </div>

      <!-- 인기 검색어 & 최근 검색어 -->
<%--      <div class="search-keywords" id="search-keywords">--%>
<%--        <div class="keywords-grid">--%>
<%--          <!-- 인기 검색어 -->--%>
<%--          <div class="keywords-section">--%>
<%--            <h3 class="keywords-title">--%>
<%--              <span class="keywords-icon">📈</span>--%>
<%--              인기 검색어--%>
<%--            </h3>--%>
<%--            <div class="keywords-list">--%>
<%--              <!-- 반복될 인기 검색어 아이템 구조 (하나만) -->--%>
<%--              <button class="keyword-item popular-keyword" data-keyword="인기검색어">--%>
<%--                <span class="keyword-rank">1</span>--%>
<%--                <span class="keyword-text">인기검색어</span>--%>
<%--              </button>--%>
<%--            </div>--%>
<%--          </div>--%>

<%--          <!-- 최근 검색어 -->--%>
<%--          <div class="keywords-section">--%>
<%--            <h3 class="keywords-title">--%>
<%--              <span class="keywords-icon">🕐</span>--%>
<%--              최근 검색어--%>
<%--            </h3>--%>
<%--            <div class="keywords-list">--%>
<%--              <!-- 반복될 최근 검색어 아이템 구조 (하나만) -->--%>
<%--              <button class="keyword-item recent-keyword" data-keyword="최근검색어">--%>
<%--                <span class="keyword-icon">#</span>--%>
<%--                <span class="keyword-text">최근검색어</span>--%>
<%--              </button>--%>
<%--            </div>--%>
<%--          </div>--%>
<%--        </div>--%>
<%--      </div>--%>
    </div>
  </section>
  <div class="results-grid">
  <c:forEach var="post" items="${searches}">
  <div class="result-card">
    <div class="card-image">
      <img src="${post.thumbnail}" alt="${post.title}" loading="lazy">
      <div class="card-badge badge-recipe">${post.type}</div>
      <div class="card-category">${post.category}</div>
    </div>
    <div class="card-content">
      <h3 class="card-title">${post.title}</h3>
      <div class="card-author">
        <svg class="author-icon" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
          <circle cx="12" cy="7" r="4"></circle>
        </svg>
      </div>
      <div class="card-meta">
        <svg class="meta-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="12" cy="12" r="10"></circle>
          <polyline points="12,6 12,12 16,14"></polyline>
        </svg>
      </div>
      <div class="card-tags">
      </div>
    </div>
  </div>
  </c:forEach>
  </div>




</main>

<!-- 푸터는 제외 -->

<script src="js/common.js"></script>
<script src="js/search.js"></script>
</body>
</html>