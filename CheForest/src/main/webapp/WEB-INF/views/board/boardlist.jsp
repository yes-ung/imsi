<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="/css/common/common.css">
    <link rel="stylesheet" href="/css/board/board.css">
</head>
<body>
<jsp:include page="/common/header.jsp"/>
    <!-- CheForest 사용자 레시피 게시판 페이지 -->
    <div class="min-h-screen bg-white">
        <!-- 페이지 헤더 -->
        <section class="bg-gradient-to-r from-pink-500 to-orange-500 text-white py-16">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
                <div class="flex items-center justify-center mb-4">
                    <i data-lucide="chef-hat" class="h-8 w-8 mr-3"></i>
                    <h1 class="text-4xl">CheForest 레시피 커뮤니티</h1>
                </div>
                <p class="text-xl opacity-90 mb-8">
                    요리 애호가들이 직접 올린 특별한 레시피들을 만나보세요
                </p>
                <div class="flex items-center justify-center space-x-8 text-sm">
                    <div class="flex items-center">
                        <i data-lucide="book-open" class="h-5 w-5 mr-2"></i>
                        <span id="totalRecipeCount">5개의 카테고리</span>
                    </div>
                    <div class="flex items-center">
                        <i data-lucide="user" class="h-5 w-5 mr-2"></i>
                        <span>커뮤니티 멤버들의 창작</span>
                    </div>
                </div>
            </div>
        </section>

        <!-- 검색 및 필터 섹션 -->
        <section class="py-8 border-b border-gray-200 bg-white">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">

                <form action="/board/list" method="get" class="flex flex-col md:flex-row gap-4 items-center justify-between">

                    <input type="hidden" name="category" value="${param.category}"/>

                    <%--검색관련 div--%>
                    <div class="flex items-center gap-2 flex-grow">
                        <!-- 정렬 옵션: name="searchType" 추가 및 선택 상태 유지 로직 추가 -->
                        <select id="boardSortSelect" name="searchType" class="border border-gray-300 rounded-lg px-3 py-2 text-sm bg-white">
                            <!-- searchType이 'title'이거나 null일 때 기본값으로 선택 -->
                            <option value="title" ${param.searchType == 'title' || param.searchType == null ? 'selected' : ''}>제목</option>
                            <!-- searchType이 'ingredient'일 때 선택 -->
                            <option value="ingredient" ${param.searchType == 'ingredient' ? 'selected' : ''}>재료</option>
                        </select>

                        <div class="relative flex-1 max-w-md">
                            <i data-lucide="search" class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500 h-4 w-4"></i>
                            <input
                                    type="text"
                                    name="keyword" value="${param.keyword}" placeholder="레시피 제목 또는 재료 검색..."
                                    class="board-search-input pl-10 pr-4 py-2 w-full border border-gray-300 rounded-lg bg-white focus:border-orange-500 focus:outline-none"
                            />
                        </div>

                        <button type="submit" class="bg-orange-500 hover:bg-orange-600 text-white px-4 py-3 rounded-lg text-sm transition-colors">
                            <i data-lucide="search" class="h-4 w-4"></i>
                        </button>
                    </div>

                    <div class="flex items-center space-x-4">
                        <button class="xl:hidden bg-gray-100 hover:bg-gray-200 text-gray-700 px-3 py-2 rounded-lg text-sm">
                            <i data-lucide="filter" class="h-4 w-4 mr-2 inline"></i>
                            필터
                        </button>
                    </div>
                </form>
            </div>
        </section>

        <!-- 레시피 목록 -->
        <section class="py-8">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div class="flex gap-6">
                    <!-- 좌측 프로필 사이드바 -->
                    <div class="w-64 hidden xl:block">
                        <div class="sticky top-8 space-y-6">
                            <!-- 내 프로필 -->
                            <div class="border-orange-200 bg-gradient-to-br from-orange-50 to-pink-50 border rounded-lg p-6">
                                <div class="flex flex-col items-center text-center space-y-3 pb-3">
                                    <!-- 로그인 했을 때 -->
                                    <sec:authorize access="isAuthenticated()">
                                        <c:choose>
                                            <c:when test="${not empty pageContext.request.userPrincipal
                                            and not empty pageContext.request.userPrincipal.principal.member.profile}">
                                                <img src="<c:url value='${pageContext.request.userPrincipal.principal.member.profile}'/>"
                                                     class="w-16 h-16 rounded-full object-cover"
                                                     alt="프로필 사진"/>
                                            </c:when>
                                            <c:otherwise>
                                                <img src="<c:url value='/images/default_profile.png'/>"
                                                     class="w-16 h-16 rounded-full object-cover"
                                                     alt="기본 프로필"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </sec:authorize>

                                    <!-- 로그인 안 했을 때 -->
                                    <sec:authorize access="isAnonymous()">
                                        <img src="<c:url value='/images/default_profile.png'/>"
                                             class="w-16 h-16 rounded-full object-cover"
                                             alt="기본 프로필"/>
                                    </sec:authorize>
                                    <div>
                                        <h3 class="text-orange-800">
                                            <c:choose>
                                                <c:when test="${pageContext.request.userPrincipal != null}">
                                                    <sec:authentication property="principal.member.nickname"/>
                                                </c:when>
                                                <c:otherwise>
                                                    로그인 후 이용해주세요.
                                                </c:otherwise>
                                            </c:choose>
                                        </h3>
                                        <div class="flex items-center justify-center space-x-1">
                                            <c:choose>
                                                <c:when test="${pageContext.request.userPrincipal.principal.member.grade eq '씨앗'}">
                                                    <img src="<c:url value='/images/grades/seed.png'/>" class="w-4 h-4" alt="씨앗"/>
                                                    <span class="text-xs text-orange-600">씨앗 등급</span>
                                                </c:when>
                                                <c:when test="${pageContext.request.userPrincipal.principal.member.grade eq '뿌리'}">
                                                    <img src="<c:url value='/images/grades/root.png'/>" class="w-4 h-4" alt="뿌리"/>
                                                    <span class="text-xs text-orange-600">뿌리 등급</span>
                                                </c:when>
                                                <c:when test="${pageContext.request.userPrincipal.principal.member.grade eq '새싹'}">
                                                    <img src="<c:url value='/images/grades/sprout.png'/>" class="w-4 h-4" alt="새싹"/>
                                                    <span class="text-xs text-orange-600">새싹 등급</span>
                                                </c:when>
                                                <c:when test="${pageContext.request.userPrincipal.principal.member.grade eq '나무'}">
                                                    <img src="<c:url value='/images/grades/tree.png'/>" class="w-4 h-4" alt="나무"/>
                                                    <span class="text-xs text-orange-600">나무 등급</span>
                                                </c:when>
                                                <c:when test="${pageContext.request.userPrincipal.principal.member.grade eq '숲'}">
                                                    <img src="<c:url value='/images/grades/flower.png'/>" class="w-4 h-4" alt="숲"/>
                                                    <span class="text-xs text-orange-600">숲 등급</span>
                                                </c:when>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                                <div class="space-y-3">
                                    <sec:authorize access="isAuthenticated()">
                                    <div class="grid grid-cols-2 gap-3 text-center">
                                        <div>
                                            <div class="text-orange-700">
                                                <c:out value="${myPostsTotalCount}" />
                                            </div>
                                            <div class="text-xs text-orange-600">작성 레시피</div>
                                        </div>
                                        <div>
                                            <div class="text-orange-700">
                                                <c:out value="${likedPostsTotalCount}" />
                                            </div>
                                            <div class="text-xs text-orange-600">좋아요한 글 수</div>
                                        </div>
                                    </div>
                                    </sec:authorize>
                                    <div class="pt-2 border-t border-orange-200 space-y-2">
                                        <sec:authorize access="isAuthenticated()">
                                        <!-- 나의 Q&A 버튼 -->
                                        <button class="w-full flex items-center justify-center px-3 py-2 bg-white border border-orange-200 hover:bg-orange-50 hover:border-orange-300 transition-all rounded-lg group">
                                            <i data-lucide="help-circle" class="h-4 w-4 text-orange-500 group-hover:text-orange-600 mr-2"></i>
                                            <span class="text-sm text-orange-600 group-hover:text-orange-700">나의 Q&A</span>
                                        </button>
                                        <!-- 마이페이지 버튼 -->
                                        <button class="w-full flex items-center justify-center px-3 py-2 bg-white border border-orange-200 hover:bg-orange-50 hover:border-orange-300 transition-all rounded-lg group" onclick="showPage('mypage')">
                                            <i data-lucide="user" class="h-4 w-4 text-orange-500 group-hover:text-orange-600 mr-2"></i>
                                            <span class="text-sm text-orange-600 group-hover:text-orange-700">마이페이지</span>
                                        </button>
                                        </sec:authorize>
                                        <!-- 로그아웃 버튼 -->
                                        <sec:authorize access="isAnonymous()">
                                            <button onclick="location.href='/auth/login'"
                                                    class="w-full flex items-center justify-center px-3 py-2 bg-white border border-orange-200 hover:bg-orange-50 hover:border-orange-300 transition-all rounded-lg group">
                                                <i data-lucide="log-in" class="h-4 w-4 text-orange-500 group-hover:text-orange-600 mr-2"></i>
                                                <span class="text-sm text-orange-600 group-hover:text-orange-700">로그인</span>
                                            </button>
                                        </sec:authorize>

                                        <sec:authorize access="isAuthenticated()">
                                            <form action="<c:url value='/auth/logout'/>" method="post">
                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                <button type="submit"
                                                        class="w-full flex items-center justify-center px-3 py-2 bg-white border border-orange-200 hover:bg-orange-50 hover:border-orange-300 transition-all rounded-lg group">
                                                    <i data-lucide="log-out" class="h-4 w-4 text-orange-500 group-hover:text-orange-600 mr-2"></i>
                                                    <span class="text-sm text-orange-600 group-hover:text-orange-700">로그아웃</span>
                                                </button>
                                            </form>
                                        </sec:authorize>
                                    </div>
                                </div>
                            </div>

                            <!-- 사이드바 - 카테고리 -->
                            <aside class="lg:w-64">
                                <div class="bg-white border border-gray-200 rounded-lg p-6 sticky top-24">
                                    <h3 class="font-semibold mb-4 flex items-center">
                                        <i data-lucide="chef-hat" class="h-5 w-5 mr-2 text-orange-500"></i>
                                        카테고리
                                    </h3>

                                    <!-- 게시판 카테고리 버튼: JS에서 동적 렌더링 -->
                                    <div class="space-y-2" id="boardCategoryButtons">
                                        <!-- board.js에서 자동으로 버튼 생성 -->
                                    </div>
                                </div>
                            </aside>

                            <!-- 나의 활동 -->
                            <sec:authorize access="isAuthenticated()">
                                <div class="bg-white border border-gray-200 rounded-lg">
                                    <div class="p-4 border-b border-gray-200">
                                        <h3 class="flex items-center">
                                            <i data-lucide="book-open" class="h-5 w-5 mr-2 text-orange-500"></i>
                                            나의 활동
                                        </h3>
                                    </div>
                                    <!-- ✅ 로그인한 경우: 통계 표시 -->
                                        <div class="p-4 space-y-3">
                                            <div class="flex justify-between items-center">
                                                <span class="text-sm text-gray-500">작성한 레시피</span>
                                                <span class="text-orange-600">
                                                    <c:out value="${myPostsTotalCount}" default="0" />개
                                                </span>
                                            </div>
                                            <div class="flex justify-between items-center">
                                                <span class="text-sm text-gray-500">받은 좋아요</span>
                                                <span class="text-orange-600">
                                                    <c:out value="${receivedLikesTotalCount}" default="0" />개
                                                </span>
                                            </div>
                                        <div class="flex justify-between items-center">
                                            <span class="text-sm text-gray-500">작성한 댓글 수</span>
                                            <span class="text-orange-600">
                                                <c:out value="${myCommentsTotalCount}" default="0" />개
                                            </span>
                                        </div>
                                    </div>
                                 </div>
                            </sec:authorize>
                        </div>
                    </div>

                    <!-- 메인 레시피 콘텐츠 -->
                    <div class="flex-1">
                        <!-- 게시판 공지사항 -->
                        <div class="mb-8 border-orange-200 bg-gradient-to-r from-orange-50 to-pink-50 border rounded-lg">
                            <div class="p-4 pb-3 border-b border-orange-200">
                                <div class="flex items-center justify-between">
                                    <h3 class="flex items-center text-orange-700">
                                        <i data-lucide="pin" class="h-5 w-5 mr-2"></i>
                                        게시판 공지사항
                                    </h3>
                                    <span class="bg-orange-500 text-white text-xs px-2 py-1 rounded">공지</span>
                                </div>
                            </div>
                            <div class="p-4">
                                <div class="space-y-4">
                                    <div class="border-l-4 border-orange-400 pl-4">
                                        <h4 class="text-orange-800 mb-2">🎉 CheForest 레시피 커뮤니티 오픈!</h4>
                                        <p class="text-sm text-orange-700 mb-2">
                                            요리 애호가들의 특별한 레시피를 공유하고 발견하는 공간이 새롭게 열렸습니다!
                                            나만의 레시피를 업로드하고 다른 요리사들과 소통해보세요.
                                        </p>
                                        <span class="text-xs text-orange-600">2024.01.15 | CheForest 관리자</span>
                                    </div>

                                    <div class="border-l-4 border-blue-400 pl-4">
                                        <h4 class="text-blue-800 mb-2">📝 레시피 작성 가이드라인</h4>
                                        <p class="text-sm text-blue-700 mb-2">
                                            더 나은 커뮤니티를 위해 레시피 작성 시 재료, 조리과정, 팁을 상세히 적어주세요.
                                            고품질 레시피는 추천 레시피로 선정됩니다!
                                        </p>
                                        <span class="text-xs text-blue-600">2024.01.12 | CheForest 관리자</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="flex items-center justify-between mb-6">
                            <div>
                                <h2 class="text-xl" id="boardCategoryTitle">전체 레시피</h2>
                                <p class="text-gray-500 mt-1" id="boardRecipeCount">
                                    <!-- 레시피 개수가 JavaScript로 동적 업데이트됩니다 -->
                                </p>
                            </div>
                            <div class="flex items-center space-x-3">
                                <button onclick="showPage('board-write')" class="btn-orange text-white px-4 py-2 rounded-lg">
                                    <i data-lucide="chef-hat" class="h-4 w-4 mr-2 inline"></i>
                                    레시피 작성하기
                                </button>
                            </div>
                        </div>

                        <!-- 인기 게시글 섹션 -->
                        <c:if test="${empty param.keyword}">
                            <div class="mb-12" id="boardPopularSection">
                                <div class="flex items-center justify-between mb-6">
                                    <h3 class="text-xl flex items-center">
                                        <i data-lucide="trending-up" class="w-6 h-6 mr-3 text-red-500"></i>
                                        인기 레시피
                                        <span class="ml-2 px-3 py-1 bg-red-100 text-red-700 text-sm rounded-full" id="boardPopularCount">
                    <!-- 인기 레시피 개수 -->
                </span>
                                    </h3>
                                    <div class="text-sm text-gray-500">
                                        ❤️ 가장 많은 좋아요를 받은 레시피
                                    </div>
                                </div>

                                <div class="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-4" id="boardPopularGrid">
                                    <c:forEach var="post" items="${bestPosts}" varStatus="status">
                                        <div class="board-popular-card bg-white border border-gray-200 rounded-lg overflow-hidden shadow-lg cursor-pointer"
                                             data-category="${post.category}"
                                             data-likes="${post.likeCount}"
                                             data-views="${post.viewCount}"
                                             data-created="${post.insertTime}"
                                             data-title="${post.title}"
                                             onclick="location.href='/board/view?boardId=${post.boardId}'">

                                            <div class="relative">
                                                <!-- 인기 순위 배지 -->
                                                <div class="absolute top-2 left-2 z-10">
                                                    <div class="board-rank-badge w-6 h-6 text-white rounded-full flex items-center justify-center text-xs">
                                                            ${status.index + 1}
                                                    </div>
                                                </div>

                                                <img src="<c:out value='${post.thumbnail}'/>"
                                                     alt="${post.title}"
                                                     class="board-card-image w-full h-48 object-cover"
                                                     onerror="this.src='/images/default_thumbnail.png'" />

                                                <!-- 상태 배지 (카테고리) -->
                                                <div class="absolute top-2 right-2 flex flex-col space-y-1">
                            <span class="category-badge
                                ${post.category eq '한식' ? ' korean' : ''}
                                ${post.category eq '양식' ? ' western' : ''}
                                ${post.category eq '중식' ? ' chinese' : ''}
                                ${post.category eq '일식' ? ' japanese' : ''}
                                ${post.category eq '디저트' ? ' dessert' : ''}">
                                <c:out value="${post.category}" />
                            </span>
                                                    <c:if test="${status.index == 0}">
                                                        <span class="bg-red-500/90 backdrop-blur-sm text-white text-xs px-2 py-1 rounded shadow-sm">HOT</span>
                                                    </c:if>
                                                </div>
                                            </div>

                                            <div class="p-4">
                                                <h4 class="board-card-title mb-2 transition-colors line-clamp-1">
                                                    <c:out value="${post.title}" />
                                                </h4>
                                                <p class="text-sm text-gray-500 mb-3 line-clamp-2">
                                                    <c:out value="${post.nickname}" /> 님의 레시피
                                                </p>

                                                <!-- 작성자 / 통계 -->
                                                <div class="flex items-center justify-between pt-3 border-t border-gray-200">
                                                    <div class="flex items-center space-x-3 text-sm">
                                                        <div class="board-stat-item flex items-center space-x-1 text-gray-500">
                                                            <i data-lucide="eye" class="h-4 w-4"></i>
                                                            <span><c:out value="${post.viewCount}" /></span>
                                                        </div>
                                                        <div class="board-stat-item flex items-center space-x-1 text-red-500">
                                                            <i data-lucide="heart" class="h-4 w-4 heart-like"></i>
                                                            <span><c:out value="${post.likeCount}" /></span>
                                                        </div>
                                                    </div>
                                                    <div class="text-xs text-gray-500">
                                                        <span><c:out value="${post.createdAgo}"/></span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>

                        <!-- 일반 게시글 섹션 -->
                        <div>
                            <div class="flex items-center justify-between mb-6">
                                <h3 class="text-xl flex items-center">
                                    <i data-lucide="book-open" class="w-6 h-6 mr-3 text-purple-500"></i>
                                    CheForest 회원들의 다양한 창작 레시피
                                    <span class="ml-2 px-3 py-1 bg-purple-100 text-purple-700 text-sm rounded-full" id="boardRegularCount">
                                    개
                                </span>
                                </h3>
                            </div>

                            <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6" id="boardRegularGrid">
                                <c:forEach var="post" items="${boards}">
                                    <div class="board-recipe-card bg-white border border-gray-200 rounded-lg overflow-hidden shadow-lg cursor-pointer"
                                         data-category="${post.category}"
                                         data-likes="${post.likeCount}"
                                         data-views="${post.viewCount}"
                                         data-created="${post.insertTime}"
                                         data-title="${post.title}"
                                         onclick="location.href='/board/view?boardId=${post.boardId}'">

                                        <div class="relative">
                                            <img src="<c:out value='${post.thumbnail}'/>"
                                                 alt="<c:out value='${post.title}'/>"
                                                 class="board-card-image w-full h-48 object-cover"
                                                 onerror="this.src='/images/default_thumbnail.png'"/>

                                            <div class="absolute top-2 right-2 flex flex-col space-y-1">
                                                <span class="category-badge
                                                    ${post.category eq '한식' ? ' korean' : ''}
                                                    ${post.category eq '양식' ? ' western' : ''}
                                                    ${post.category eq '중식' ? ' chinese' : ''}
                                                    ${post.category eq '일식' ? ' japanese' : ''}
                                                    ${post.category eq '디저트' ? ' dessert' : ''}">
                                                    <c:out value="${post.category}" />
                                                </span>
                                                    <%-- HOT/NEW 뱃지는 필요에 따라 여기에 추가할 수 있습니다. --%>
                                            </div>
                                        </div>

                                        <div class="p-4">
                                            <h4 class="board-card-title mb-2 transition-colors line-clamp-1">
                                                <c:out value="${post.title}"/>
                                            </h4>

                                            <p class="text-sm text-gray-500 mb-3 line-clamp-2">
                                                <c:out value="${post.nickname}"/> 님의 레시피
                                            </p>

                                            <div class="flex items-center justify-between pt-3 border-t border-gray-200">
                                                <div class="flex items-center space-x-3 text-sm">
                                                    <div class="flex items-center space-x-1 text-gray-500">
                                                        <i data-lucide="eye" class="h-4 w-4"></i>
                                                        <span><c:out value="${post.viewCount}"/></span>
                                                    </div>
                                                    <div class="flex items-center space-x-1 text-red-500">
                                                        <i data-lucide="heart" class="h-4 w-4 heart-like"></i>
                                                        <span><c:out value="${post.likeCount}"/></span>
                                                    </div>
                                                </div>

                                                <div class="text-xs text-gray-400">
                                                    <c:out value="${post.createdAgo}"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>

                                <div class="col-span-full text-center py-12"
                                     id="boardNoResultsSection"
                                     style="display: none;">
                                    <i data-lucide="chef-hat" class="h-16 w-16 text-gray-400 mx-auto mb-4"></i>
                                    <h3 class="text-xl mb-2">검색 결과가 없습니다</h3>
                                    <p class="text-gray-500">다른 검색어나 카테고리를 시도해보세요.</p>
                                </div>
                            </div>
                            <div class="mt-8 flex justify-center">
                                <c:if test="${pageInfo.totalPages > 1}">
                                    <nav class="flex space-x-2">

                                        <c:set var="totalPagesSafe" value="${empty pageInfo.totalPages ? 0 : pageInfo.totalPages}" />
                                        <c:set var="currentPageSafe" value="${empty pageInfo.number ? 0 : pageInfo.number}" />
                                        <c:set var="sizeSafe" value="${empty param.size ? 9 : param.size}" />

                                        <c:set var="blockSize" value="10"/>
                                        <c:set var="blockStart" value="${currentPageSafe - (currentPageSafe mod blockSize)}"/>
                                        <c:set var="blockEnd" value="${blockStart + blockSize - 1}"/>
                                        <c:if test="${blockEnd >= totalPagesSafe - 1}">
                                            <c:set var="blockEnd" value="${totalPagesSafe - 1}"/>
                                        </c:if>

                                        <!-- ◀ 이전 블록 -->
                                        <c:set var="prevBlockPage" value="${blockStart - blockSize}"/>
                                        <c:if test="${prevBlockPage < 0}">
                                            <c:set var="prevBlockPage" value="0"/>
                                        </c:if>
                                        <c:url var="prevBlockUrl" value="/board/list">
                                            <c:param name="page" value="${prevBlockPage}"/>
                                            <c:param name="size" value="${sizeSafe}"/>
                                            <c:param name="category" value="${param.category}"/>
                                            <c:param name="keyword" value="${param.keyword}"/>
                                        </c:url>
                                        <a href="${prevBlockUrl}" class="px-3 py-1 border rounded ${blockStart==0?'pointer-events-none opacity-50':''}">«</a>

                                        <!-- 페이지 번호 -->
                                        <c:forEach var="i" begin="${blockStart}" end="${blockEnd}">
                                            <c:url var="pageUrl" value="/board/list">
                                                <c:param name="page" value="${i}"/>
                                                <c:param name="size" value="${sizeSafe}"/>
                                                <c:param name="category" value="${param.category}"/>
                                                <c:param name="keyword" value="${param.keyword}"/>
                                            </c:url>
                                            <a href="${pageUrl}"
                                               class="px-3 py-1 border rounded
                                                    ${currentPageSafe == i ? 'bg-orange-500 text-white border-orange-500' : 'bg-white text-gray-700'}">
                                                    ${i + 1}
                                            </a>
                                        </c:forEach>

                                        <!-- › 다음 블록 -->
                                        <c:set var="nextBlockPage" value="${blockStart + blockSize}"/>
                                        <c:if test="${nextBlockPage > totalPagesSafe - 1}">
                                            <c:set var="nextBlockPage" value="${totalPagesSafe - 1}"/>
                                        </c:if>
                                        <c:url var="nextBlockUrl" value="/board/list">
                                            <c:param name="page" value="${nextBlockPage}"/>
                                            <c:param name="size" value="${sizeSafe}"/>
                                            <c:param name="category" value="${param.category}"/>
                                            <c:param name="keyword" value="${param.keyword}"/>
                                        </c:url>
                                        <a href="${nextBlockUrl}" class="px-3 py-1 border rounded ${(blockStart + blockSize) > (totalPagesSafe - 1) ? 'pointer-events-none opacity-50':''}">»</a>
                                    </nav>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
        </section>
    </div>
    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- Lucide Icons -->
    <script src="https://unpkg.com/lucide@latest/dist/umd/lucide.js"></script>
    <script src="/js/common/common.js"></script>
    <script src="/js/board/board.js"></script>

    <!-- 게시판 콘텐츠 로드 -->
<%--<!-- ===== 챗 UI + JS 연결 (푸터 위로 이동) ===== -->--%>
<jsp:include page="/WEB-INF/views/chat/chat.jsp" />
    <script>
        async function loadBoardContent() {
            try {
                // board.html 로드
                const response = await fetch('board.html');
                const boardHTML = await response.text();
                document.getElementById('board-content-container').innerHTML = boardHTML;

                // Lucide 아이콘 초기화
                lucide.createIcons();

            } catch (error) {
                console.error('❌ 게시판 페이지 로드 중 오류 발생:', error);
            }
        }

        // 페이지 로드 시 실행
        document.addEventListener('DOMContentLoaded', loadBoardContent);
    </script>
<jsp:include page="/common/footer.jsp"/>

</body>
</html>