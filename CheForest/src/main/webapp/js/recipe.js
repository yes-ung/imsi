/* CheForest Recipe Page JavaScript */
/* 레시피 페이지 전용 JavaScript (카드 필터링 + 검색만 담당) */

// 전역 변수
let selectedCategory = 'all';
let searchQuery = '';
let sortBy = 'popularity';

// HTML에서 레시피 카드 요소들 가져오기
function getRecipeCards() {
    const popularCards = Array.from(document.querySelectorAll('#popularGrid .popular-recipe-card'));
    const regularCards = Array.from(document.querySelectorAll('#regularGrid .recipe-card'));
    return { popular: popularCards, regular: regularCards, all: [...popularCards, ...regularCards] };
}

// 카드 필터링
function shouldShowRecipeCard(card) {
    const category = card.dataset.category;
    const title = card.dataset.title?.toLowerCase() || '';
    const description = card.dataset.description?.toLowerCase() || '';
    const matchesCategory = selectedCategory === 'all' || category === selectedCategory;
    const searchLower = searchQuery.toLowerCase();
    const matchesSearch = !searchQuery || title.includes(searchLower) || description.includes(searchLower);
    return matchesCategory && matchesSearch;
}

// 카드 정렬
function sortRecipeCards(cards) {
    return cards.sort((a, b) => {
        const aLikes = parseInt(a.dataset.likes) || 0;
        const bLikes = parseInt(b.dataset.likes) || 0;
        const aViews = parseInt(a.dataset.views) || 0;
        const bViews = parseInt(b.dataset.views) || 0;
        const aCreated = new Date(a.dataset.created).getTime();
        const bCreated = new Date(b.dataset.created).getTime();
        switch (sortBy) {
            case 'popularity': return bLikes - aLikes;
            case 'views': return bViews - aViews;
            case 'newest': return bCreated - aCreated;
            default: return 0;
        }
    });
}

// 인기 레시피 렌더링
function renderPopularRecipes() {
    const cards = getRecipeCards();
    const popularSection = document.getElementById('popularSection');
    const popularCount = document.getElementById('popularCount');
    let visibleCount = 0;
    cards.popular.forEach(card => {
        if (shouldShowRecipeCard(card)) { card.style.display = 'block'; visibleCount++; }
        else card.style.display = 'none';
    });
    if (visibleCount > 0) {
        popularSection.style.display = 'block';
        popularCount.textContent = `TOP ${visibleCount}`;
    } else {
        popularSection.style.display = 'none';
    }
}

// 일반 레시피 렌더링
function renderRegularRecipes() {
    const cards = getRecipeCards();
    const regularCount = document.getElementById('regularCount');
    const visibleCards = cards.regular.filter(card => shouldShowRecipeCard(card));
    const sortedCards = sortRecipeCards([...visibleCards]);
    cards.regular.forEach(card => card.style.display = 'none');
    const regularGrid = document.getElementById('regularGrid');
    if (regularGrid && sortedCards.length > 0) {
        sortedCards.forEach(card => {
            card.style.display = 'block';
            regularGrid.appendChild(card);
        });
    }
    regularCount.textContent = `${visibleCards.length}개`;
}

// 결과 없음 처리
function toggleNoResultsSection() {
    const cards = getRecipeCards();
    const noResultsSection = document.getElementById('noResultsSection');
    const loadMoreSection = document.getElementById('loadMoreSection');
    const visiblePopular = cards.popular.filter(card => shouldShowRecipeCard(card)).length;
    const visibleRegular = cards.regular.filter(card => shouldShowRecipeCard(card)).length;
    if (visiblePopular === 0 && visibleRegular === 0) {
        noResultsSection.style.display = 'block';
        loadMoreSection.style.display = 'none';
    } else {
        noResultsSection.style.display = 'none';
        loadMoreSection.style.display = 'block';
    }
}

// 레시피 개수 업데이트
function updateRecipeCount() {
    const cards = getRecipeCards();
    const recipeCount = document.getElementById('recipeCount');
    const visiblePopular = cards.popular.filter(card => shouldShowRecipeCard(card)).length;
    const visibleRegular = cards.regular.filter(card => shouldShowRecipeCard(card)).length;
    const totalVisible = visiblePopular + visibleRegular;
    if (recipeCount) {
        recipeCount.textContent = `총 ${totalVisible}개의 레시피 • 인기 ${visiblePopular}개, 일반 ${visibleRegular}개`;
    }
}

// 카테고리 전환
function switchRecipeCategory(categoryId) {
    selectedCategory = categoryId;
    updateRecipeContent();
}

// 검색 처리
function handleRecipeSearch() {
    const searchInput = document.getElementById('recipeSearchInput');
    if (searchInput) {
        searchQuery = searchInput.value.trim();
        updateRecipeContent();
    }
}

// 레시피 전체 갱신
function updateRecipeContent() {
    updateRecipeCount();
    renderPopularRecipes();
    renderRegularRecipes();
    toggleNoResultsSection();
}

// 검색 이벤트 바인딩
function setupRecipeSearchEvents() {
    const searchInput = document.getElementById('recipeSearchInput');
    if (searchInput) {
        let searchTimeout;
        searchInput.addEventListener('input', function() {
            clearTimeout(searchTimeout);
            searchTimeout = setTimeout(() => { handleRecipeSearch(); }, 300);
        });
        searchInput.addEventListener('keydown', function(event) {
            if (event.key === 'Enter') {
                clearTimeout(searchTimeout);
                handleRecipeSearch();
            }
        });
    }
}

// 초기화
function initializeRecipePage() {
    updateRecipeContent();
    setupRecipeSearchEvents();
    console.log('✅ 레시피 페이지 초기화 완료!');
}

document.addEventListener('DOMContentLoaded', initializeRecipePage);

// 전역 등록
window.CheForest = window.CheForest || {};
window.CheForest.recipe = { switchRecipeCategory, handleRecipeSearch, updateRecipeContent };
