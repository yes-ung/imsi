/* CheForest - boardview.js (댓글/답글 + 페이지네이션 고정본)
 * - 탭 전환 / 좋아요·북마크 프런트 토글 / Lucide 초기화
 * - 댓글/대댓글 트리 렌더링 (닉네임 정확히 노출, 액션 정렬 고정, 중복 수정폼 방지)
 * - 상위 댓글 페이지네이션(3개/페이지), 대댓글은 그대로 하위에 표시
 * - ReviewController API
 *   · 목록:   GET  /reviews/board/{boardId}
 *   · 등록:   POST /reviews  (body: {boardId, writerIdx, content [,parentId]})
 *   · 수정:   PUT  /reviews/{reviewId} (body: {content})
 *   · 삭제:   DELETE /reviews/{reviewId}
 */

(function () {
    // ========= 전역 값 (JSP에서 내려줌) =========
    const boardId = window.boardId || 0;
    const loginUser = window.loginUser || { memberIdx: 0, nickname: "익명" };

    // ========= CSRF =========
    const csrfToken  = document.querySelector('meta[name="_csrf"]')?.content || "";
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || "X-CSRF-TOKEN";
    const withCsrf = (headers = {}) => (csrfToken ? { ...headers, [csrfHeader]: csrfToken } : headers);

    // ========= DOM 캐시 =========
    const $commentsList = document.getElementById("commentsList");
    const $commentBtn   = document.getElementById("commentSubmitBtn");
    const $commentTa    = document.getElementById("commentTextarea");
    const $pager        = document.getElementById("commentsPager");

    // ========= API =========
    const API = {
        list:   ()             => `/reviews/board/${boardId}`,
        create: ()             => `/reviews`,
        update: (reviewId)     => `/reviews/${reviewId}`,
        remove: (reviewId)     => `/reviews/${reviewId}`,
    };

    // ========= 유틸 =========
    const getProfile = (p) => (p && String(p).trim() ? p : "/images/default_profile.png");
    const esc  = (s) => String(s ?? "").replaceAll("&","&amp;").replaceAll("<","&lt;").replaceAll(">","&gt;");
    const br   = (s) => esc(s).replaceAll("\n","<br/>");

    // 서버 ReviewDto → 화면 모델 정규화 (닉네임 필드 최우선 매핑)
    function normalize(d) {
        return {
            id: d.reviewId,
            text: d.content ?? d.text ?? "",
            likeCount: d.likeCount ?? 0,
            createdAt: d.createdAt ?? d.insertTimeStr ?? "",
            writer: {
                memberIdx: d.writerIdx ?? d.writer?.memberIdx,
                nickname:
                    d.writerNickname ??
                    d.writer?.nickname ??
                    d.nickname ??
                    d.memberNickname ??
                    "익명",
                profile:   d.writerProfile ?? d.writer?.profile ?? "",
                grade:     d.writerGrade ?? d.writer?.grade ?? ""
            },
            replies: Array.isArray(d.replies) ? d.replies.map(normalize) : []
        };
    }

    // ========= 아이콘 (간단 inline) =========
    const iconThumb = '<svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor"><path d="M7 10v11H4a2 2 0 0 1-2-2v-7a2 2 0 0 1 2-2z"/><path d="M7 10l4-8 2 5h5.5a2.5 2.5 0 0 1 0 5H17l1 5a2 2 0 0 1-2 2H7"/></svg>';
    const iconReply = '<svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor"><path d="M3 12h13l-4-4m4 4-4 4"/></svg>';

    // ========= 렌더러 =========
    function renderReply(r) {
        const mine = Number(loginUser.memberIdx||0) === Number(r.writer.memberIdx||-1);
        return `
    <div class="reply-item" data-reply-id="${r.id}" data-raw="${esc(r.text)}">
      <div class="reply-border">
        <div class="reply-avatar">
          <img src="${getProfile(r.writer.profile)}" alt="avatar" class="avatar-img" onerror="this.src='/images/default_profile.png'"/>
        </div>
        <div class="reply-details">
          <div class="name-row">
            <span class="reply-author">${esc(r.writer.nickname)}</span>
            ${r.writer.grade ? `<span class="user-grade grade-${esc(r.writer.grade)}">${esc(r.writer.grade)}</span>` : ``}
            <span class="reply-time">${esc(r.createdAt)}</span>
          </div>
          <div class="reply-text"><div class="reply-plain">${br(r.text)}</div></div>

          <div class="reply-actions">
            <button class="like-vote" data-act="reply-like">${iconThumb}<span class="vote-count">${r.likeCount||0}</span></button>
            <div class="right-actions">
              ${mine ? `
                <button class="edit-btn small" data-act="reply-edit">수정</button>
                <button class="delete-btn small" data-act="reply-delete">삭제</button>` : ``}
            </div>
          </div>
        </div>
      </div>
    </div>`;
    }

    function renderComment(c) {
        const mine = Number(loginUser.memberIdx||0) === Number(c.writer.memberIdx||-1);
        return `
    <div class="comment-item" data-comment-id="${c.id}" data-raw="${esc(c.text)}">
      <div class="comment-header">
        <div class="commenter-avatar">
          <img src="${getProfile(c.writer.profile)}" alt="avatar" class="avatar-img" onerror="this.src='/images/default_profile.png'"/>
        </div>
        <div class="commenter-details">
          <div class="name-row">
            <span class="commenter-name">${esc(c.writer.nickname)}</span>
            ${c.writer.grade ? `<span class="user-grade grade-${esc(c.writer.grade)}">${esc(c.writer.grade)}</span>` : ``}
            <span class="comment-time">${esc(c.createdAt)}</span>
          </div>
          <div class="comment-body"><div class="comment-plain">${br(c.text)}</div></div>
        </div>
      </div>

      <div class="comment-actions">
        <button class="like-vote" data-act="like">${iconThumb}<span class="vote-count">${c.likeCount||0}</span></button>
        <button class="reply-btn" data-act="toggle-reply">${iconReply}답글</button>
        <div class="right-actions">
          ${mine ? `
            <button class="edit-btn" data-act="edit">수정</button>
            <button class="delete-btn" data-act="delete">삭제</button>` : ``}
        </div>
      </div>

      <div class="reply-write" style="display:none;">
        <textarea class="reply-textarea" placeholder="답글을 입력하세요"></textarea>
        <div class="reply-actions">
          <button class="reply-submit-btn" data-act="reply-submit">답글 등록</button>
          <button class="cancel-btn" data-act="reply-cancel">취소</button>
        </div>
      </div>

      <div class="replies">${c.replies.map(renderReply).join("")}</div>
    </div>`;
    }

    // ========= 페이지네이션 상태 =========
    const PAGE_SIZE = 3;     // 한 페이지당 상위 댓글 3개
    let   ALL_COMMENTS = []; // 상위 댓글 트리 전체
    let   CURRENT_PAGE = 1;

    function renderCurrentPage() {
        if (!$commentsList) return;

        const totalTop   = ALL_COMMENTS.length;
        const totalPages = Math.max(1, Math.ceil(totalTop / PAGE_SIZE));
        if (CURRENT_PAGE > totalPages) CURRENT_PAGE = totalPages;

        const start = (CURRENT_PAGE - 1) * PAGE_SIZE;
        const end   = start + PAGE_SIZE;

        const pageSlice = ALL_COMMENTS.slice(start, end);
        $commentsList.innerHTML = pageSlice.map(renderComment).join("");

        renderPager(totalPages);
    }

    function renderPager(totalPages) {
        if (!$pager) return;
        if (totalPages <= 1) { $pager.innerHTML = ""; return; }

        const STEP = 10;
        const canPrev = (CURRENT_PAGE - STEP) >= 1;
        const canNext = (CURRENT_PAGE + STEP) <= totalPages;

        let html = "";
        html += `<button data-page="prev" ${!canPrev ? "disabled": ""}>«</button>`;
        for (let p = 1; p <= totalPages; p++) {
            html += `<button data-page="${p}" class="${p===CURRENT_PAGE ? "active": ""}">${p}</button>`;
        }
        html += `<button data-page="next" ${!canNext ? "disabled": ""}>»</button>`;
        $pager.innerHTML = html;
    }


    function initPagerEvents() {
        if (!$pager) return;
        $pager.addEventListener("click", (e) => {
            const btn = e.target.closest("button[data-page]");
            if (!btn) return;
            const val = btn.getAttribute("data-page");
            const totalPages = Math.max(1, Math.ceil(ALL_COMMENTS.length / PAGE_SIZE));
            const STEP = 10;

            if (val === "prev") {
                CURRENT_PAGE = Math.max(1, CURRENT_PAGE - STEP);
            } else if (val === "next") {
                CURRENT_PAGE = Math.min(totalPages, CURRENT_PAGE + STEP);
            } else {
                const num = parseInt(val, 10);
                if (!isNaN(num)) CURRENT_PAGE = num;
            }
            renderCurrentPage();
        });
    }


    // ========= 데이터 통신 =========
    async function loadComments() {
        if (!$commentsList) return;
        try {
            const res = await fetch(API.list(), { method: "GET" });
            if (!res.ok) throw new Error(res.status);
            const data = await res.json();

            ALL_COMMENTS = Array.isArray(data) ? data.map(normalize) : [];
            if (ALL_COMMENTS.length === 0) {
                $commentsList.innerHTML = `<div class="empty-box">아직 댓글이 없습니다.</div>`;
                if ($pager) $pager.innerHTML = "";
                return;
            }
            CURRENT_PAGE = 1;        // 첫 로드 1페이지
            renderCurrentPage();
        } catch (e) {
            console.error(e);
            $commentsList.innerHTML = `<div class="empty-box">댓글을 불러오지 못했습니다.</div>`;
            if ($pager) $pager.innerHTML = "";
        }
    }

    // 등록/수정/삭제 후 현재 페이지 유지
    async function reloadKeepPage() {
        const prevPage = CURRENT_PAGE;
        try {
            const res = await fetch(API.list(), { method: "GET" });
            if (!res.ok) throw new Error(res.status);
            const data = await res.json();
            ALL_COMMENTS = Array.isArray(data) ? data.map(normalize) : [];

            const totalPages = Math.max(1, Math.ceil(ALL_COMMENTS.length / PAGE_SIZE));
            CURRENT_PAGE = Math.min(prevPage, totalPages);
            renderCurrentPage();
        } catch (e) {
            console.error(e);
            loadComments();
        }
    }

    // ========= 기타 UI =========
    function initTabs() {
        const triggers = document.querySelectorAll(".tab-trigger");
        const contents = document.querySelectorAll(".tab-content");
        if (!triggers.length || !contents.length) return;
        triggers.forEach(btn => {
            btn.addEventListener("click", () => {
                const key = btn.getAttribute("data-tab");
                triggers.forEach(b => b.classList.remove("active"));
                contents.forEach(c => c.classList.remove("active"));
                btn.classList.add("active");
                const target = document.getElementById(`${key}Content`);
                if (target) target.classList.add("active");
            });
        });
    }

    function initReactions() {
        const likeBtn = document.getElementById("likeBtn");
        const bookmarkBtn = document.getElementById("bookmarkBtn");
        if (likeBtn) likeBtn.addEventListener("click", () => likeBtn.classList.toggle("liked"));
        if (bookmarkBtn) bookmarkBtn.addEventListener("click", () => bookmarkBtn.classList.toggle("bookmarked"));
    }

    function initIcons() {
        if (window.lucide && typeof window.lucide.createIcons === "function") {
            window.lucide.createIcons();
        }
    }

    // ========= 댓글 등록 =========
    function initCommentCreate() {
        if (!$commentBtn || !$commentTa) return;
        $commentBtn.addEventListener("click", async () => {
            const text = ($commentTa.value || "").trim();
            if (!text) return alert("댓글을 입력하세요.");
            try {
                await createReview({ content: text });
                $commentTa.value = "";
                await reloadKeepPage();     // 페이지 유지
            } catch (e) {
                console.error(e);
                alert("댓글 등록 실패");
            }
        });
    }

    // ========= 통신 함수 =========
    async function createReview({ content, parentId = null }) {
        const payload = { boardId, writerIdx: loginUser.memberIdx, content };
        if (parentId) payload.parentId = parentId;
        const res = await fetch(API.create(), {
            method: "POST",
            headers: withCsrf({ "Content-Type": "application/json" }),
            body: JSON.stringify(payload),
        });
        if (!res.ok) throw new Error(res.status);
        return res.json();
    }

    async function updateReview(reviewId, content) {
        const res = await fetch(API.update(reviewId), {
            method: "PUT",
            headers: withCsrf({ "Content-Type": "application/json" }),
            body: JSON.stringify({ content }),
        });
        if (!res.ok) throw new Error(res.status);
        return res.json();
    }

    async function deleteReview(reviewId) {
        const res = await fetch(API.remove(reviewId), {
            method: "DELETE",
            headers: withCsrf(),
        });
        if (!res.ok) throw new Error(res.status);
    }

    // ========= 델리게이션 (수정/삭제/답글) =========
    function initCommentDelegation() {
        if (!$commentsList) return;

        $commentsList.addEventListener("click", async (e) => {
            const $btn = e.target.closest("button,[data-act]");
            if (!$btn) return;
            const act = $btn.dataset.act;
            const $comment = e.target.closest(".comment-item");
            const $reply = e.target.closest(".reply-item");

            try {
                // ===== 댓글 =====
                if ($comment) {
                    const id = Number($comment.dataset.commentId);

                    if (act === "like") {
                        const $cnt = $btn.querySelector(".vote-count");
                        if ($cnt) $cnt.textContent = String(Number($cnt.textContent || 0) + 1);
                        return;
                    }

                    if (act === "toggle-reply") {
                        const $p = $comment.querySelector(".reply-write");
                        if ($p) $p.style.display = ($p.style.display === "none" || !$p.style.display) ? "block" : "none";
                        return;
                    }

                    if (act === "reply-submit") {
                        const $ta = $comment.querySelector(".reply-textarea");
                        const text = ($ta?.value || "").trim();
                        if (!text) return alert("답글을 입력하세요.");
                        await createReview({ content: text, parentId: id });
                        await reloadKeepPage();
                        return;
                    }

                    if (act === "reply-cancel") {
                        const $p = $comment.querySelector(".reply-write");
                        if ($p) $p.style.display = "none";
                        return;
                    }

                    if (act === "edit") {
                        if ($comment.dataset.editing === "1") return; // 중복 방지
                        $comment.dataset.editing = "1";
                        const raw = $comment.dataset.raw ?? $comment.querySelector(".comment-plain")?.textContent ?? "";
                        const $body = $comment.querySelector(".comment-body");
                        // 중복 생성 방지(이미 textarea 있으면 종료)
                        if ($body.querySelector(".edit-textarea")) return;
                        $body.innerHTML = `
              <textarea class="edit-textarea">${esc(raw)}</textarea>
              <div class="edit-actions">
                <button class="cancel-btn" data-act="edit-cancel">취소</button>
                <button class="save-btn" data-act="edit-save">저장</button>
              </div>`;
                        return;
                    }

                    if (act === "edit-cancel") {
                        delete $comment.dataset.editing;
                        await reloadKeepPage();
                        return;
                    }

                    if (act === "edit-save") {
                        const $ta = $comment.querySelector(".edit-textarea");
                        const text = ($ta?.value || "").trim();
                        if (!text) return alert("내용을 입력하세요.");
                        await updateReview(id, text);
                        delete $comment.dataset.editing;
                        await reloadKeepPage();
                        return;
                    }

                    if (act === "delete") {
                        if (!confirm("댓글을 삭제할까요?")) return;
                        await deleteReview(id);
                        await reloadKeepPage();
                        return;
                    }
                }

                // ===== 대댓글 =====
                if ($reply) {
                    const rid = Number($reply.dataset.replyId);

                    if (act === "reply-like") {
                        const $cnt = $btn.querySelector(".vote-count");
                        if ($cnt) $cnt.textContent = String(Number($cnt.textContent || 0) + 1);
                        return;
                    }

                    if (act === "reply-edit") {
                        if ($reply.dataset.editing === "1") return; // 중복 방지
                        $reply.dataset.editing = "1";
                        const raw = $reply.dataset.raw ?? $reply.querySelector(".reply-plain")?.textContent ?? "";
                        const $txt = $reply.querySelector(".reply-text");
                        if ($txt.querySelector(".edit-textarea")) return; // 중복 방지
                        $txt.innerHTML = `
              <textarea class="edit-textarea">${esc(raw)}</textarea>
              <div class="edit-actions">
                <button class="cancel-btn" data-act="reply-edit-cancel">취소</button>
                <button class="save-btn" data-act="reply-edit-save">저장</button>
              </div>`;
                        return;
                    }

                    if (act === "reply-edit-cancel") {
                        delete $reply.dataset.editing;
                        await reloadKeepPage();
                        return;
                    }

                    if (act === "reply-edit-save") {
                        const $ta = $reply.querySelector(".edit-textarea");
                        const text = ($ta?.value || "").trim();
                        if (!text) return alert("내용을 입력하세요.");
                        await updateReview(rid, text);
                        delete $reply.dataset.editing;
                        await reloadKeepPage();
                        return;
                    }

                    if (act === "reply-delete") {
                        if (!confirm("대댓글을 삭제할까요?")) return;
                        await deleteReview(rid);
                        await reloadKeepPage();
                        return;
                    }
                }
            } catch (err) {
                console.error(err);
                alert("처리 중 오류가 발생했습니다.");
            }
        });
    }

    // ========= 초기화 =========
    document.addEventListener("DOMContentLoaded", () => {
        initTabs();
        initReactions();
        initIcons();

        initPagerEvents();     // 페이지네이션 이벤트
        initCommentCreate();
        initCommentDelegation();

        loadComments();
    });
})();
