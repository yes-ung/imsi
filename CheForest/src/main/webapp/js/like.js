function initLikeButton({ likeType, boardId, recipeId, memberIdx }) {
  const $btn       = $("#likeBtn");
  const $countText = $("#likeCountText");

  // ★ 숫자 강제 변환 + 유효성 플래그
  const nMemberIdx = Number(memberIdx);
  const hasMember  = Number.isFinite(nMemberIdx) && nMemberIdx > 0;

  // ★ 아이디들도 숫자화 (NaN이면 undefined로)
  const nBoardId  = boardId  != null && String(boardId).trim()  !== "" ? Number(boardId)  : undefined;
  const nRecipeId = recipeId != null && String(recipeId).trim() !== "" ? Number(recipeId) : undefined;

  // 🚀 countText 업데이트 헬퍼
  function renderCount(n) {
    const safe = (isNaN(n) || n < 0) ? 0 : n;
    if (likeType === "BOARD") {
      $countText.html(`좋아요 <span>${safe}</span>개`);
    } else {
      $countText.text(safe);
    }
  }

  // 1) 초기 좋아요 수 불러오기 (board/recipe에 맞는 키만 전달)
  $.get("/like/count", { likeType, boardId: nBoardId, recipeId: nRecipeId }, renderCount);

  // 2) 로그인된 경우 → 상태 확인 (숫자 memberIdx만 허용)
  if (hasMember) {
    $.get(
        "/like/check",
        { likeType, boardId: nBoardId, recipeId: nRecipeId, memberIdx: nMemberIdx },
        function (res) {
          if (res === true || res === "true") {
            $btn.text("♥").addClass("liked");
          }
        }
    );
  }

  // 3) 클릭 이벤트
  $btn.on("click", function () {
    if (!hasMember) {
      alert("로그인 후 이용해주세요 😊");
      const redirectUrl = encodeURIComponent(location.pathname + location.search);
      return (location.href = "/member/login?redirect=" + redirectUrl);
    }
    if (likeType === "BOARD" && !Number.isFinite(nBoardId)) {
      console.warn("🚫 boardId 없음/비정상 → 좋아요 요청 막음");
      return;
    }
    if (likeType === "RECIPE" && !Number.isFinite(nRecipeId)) {
      console.warn("🚫 recipeId 없음/비정상 → 좋아요 요청 막음");
      return;
    }

    const isLiked = $btn.hasClass("liked");
    const url     = isLiked ? "/like/remove" : "/like/add";

    $.ajax({
      url,
      type: "POST",
      contentType: "application/json",
      // ★ 서버에 숫자로 전달
      data: JSON.stringify({ likeType, boardId: nBoardId, recipeId: nRecipeId, memberIdx: nMemberIdx }),
      success: function (res) {
        renderCount(res.likeCount);
        $btn.text(isLiked ? "♡" : "♥").toggleClass("liked");
      },
      error: function (xhr) {
        console.error("좋아요 요청 실패:", xhr.responseText);
      }
    });
  });
}
