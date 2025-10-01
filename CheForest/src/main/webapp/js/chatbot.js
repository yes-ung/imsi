// === 전역 변수 ===
let chatbotBtn, chatbotWindow, closeBtn, msgBox, userInput, sendBtn, toggleBtn, quickBtns;

// ✅ JSP에서 contextPath 전달받기 (jsp 상단에 <script>로 const contextPath 정의 필요)
if (typeof contextPath === "undefined") {
    var contextPath = "";
}

// === 메시지 출력 ===
function appendUserMessage(text) {
    const userMsg = document.createElement("div");
    userMsg.className = "message user-msg";
    userMsg.innerHTML = `<div class="bubble">${text}</div>`;
    msgBox.appendChild(userMsg);
    msgBox.scrollTop = msgBox.scrollHeight;
}

function appendBotMessage(html) {
    const botMsg = document.createElement("div");
    botMsg.className = "message bot-msg";
    botMsg.innerHTML = `
        <img src="${contextPath}/images/bear-mascot.png" alt="셰프봇" class="bot-avatar">
        <div class="bubble">${html}</div>
    `;
    msgBox.appendChild(botMsg);
    msgBox.scrollTop = msgBox.scrollHeight;
}

// === 카드 UI 출력 ===
function renderCards(data, type) {
    let reply = `<div class="card-container">`;

    data.forEach(item => {
        if (type === "recipe") {
            reply += `
<div class="recipe-card"
     onclick="window.open('${contextPath}/recipe/view?recipeId=${item.recipeId}', '_blank')"
     style="cursor:pointer; text-align:center;">
  <img src="${item.thumbnail ? item.thumbnail : contextPath + '/images/default-recipe.png'}"
       alt="${item.titleKr}"
       style="width:100%; height:160px; object-fit:cover; border-radius:8px;"/>
  <h3 style="margin:8px 0 0 0;">${item.titleKr}</h3>
</div>`;
        }
        if (type === "board") {
            reply += `
<div class="board-card"
     onclick="window.open('${contextPath}/board/view?boardId=${item.boardId}', '_blank')"
     style="cursor:pointer; text-align:center;">
  <img src="${item.thumbnail ? item.thumbnail : contextPath + '/images/default-board.png'}"
       alt="${item.title}"
       style="width:100%; height:160px; object-fit:cover; border-radius:8px;"/>
  <h3 style="margin:8px 0 0 0;">${item.title}</h3>
</div>`;
        }
    });

    reply += `</div>`;
    appendBotMessage(reply);
}

// === 랜덤 레시피 불러오기 (단일) ===
function getRandomRecipe(category) {
    $.ajax({
        url: contextPath + "/recipe/recommendRecipe.do",
        type: "GET",
        data: { category: category },
        dataType: "json",
        success: function(data) {
            if (!data) {
                appendBotMessage(`❌ "${category}" 추천 레시피를 찾을 수 없습니다.`);
                return;
            }

            let card = `
<div class="recipe-card"
     onclick="window.open('${contextPath}/recipe/view?recipeId=${data.recipeId}', '_blank')"
     style="cursor:pointer; text-align:center;">
  <img src="${data.thumbnail ?? contextPath + '/images/default-recipe.png'}"
       alt="${data.titleKr}"
       style="width:100%; height:160px; object-fit:cover; border-radius:8px;">
  <h3 style="margin:8px 0 0 0;">${data.titleKr}</h3>
</div>`;
            appendBotMessage(card);
        },
        error: function(xhr, status, err) {
            console.error(err);
            appendBotMessage("❌ 추천 레시피를 불러오는 중 오류가 발생했습니다.");
        }
    });
}

// === 카테고리 버튼 출력 ===
function showCategoryButtons(categories, actionType) {
    const botMsg = document.createElement("div");
    botMsg.className = "message bot-msg";
    const bubble = document.createElement("div");
    bubble.className = "bubble";

    const catDiv = document.createElement("div");
    catDiv.style.display = "flex";
    catDiv.style.flexWrap = "wrap";
    catDiv.style.gap = "6px";
    catDiv.style.marginTop = "8px";

    categories.forEach(cat => {
        const catBtn = document.createElement("button");
        catBtn.textContent = cat;
        catBtn.style.padding = "6px 10px";
        catBtn.style.border = "1px solid #ccc";
        catBtn.style.borderRadius = "6px";
        catBtn.style.cursor = "pointer";

        catBtn.onclick = () => {
            appendUserMessage(cat);
            getRandomRecipe(cat);
        };

        catDiv.appendChild(catBtn);
    });

    bubble.appendChild(catDiv);
    botMsg.appendChild(bubble);
    msgBox.appendChild(botMsg);
    msgBox.scrollTop = msgBox.scrollHeight;
}

// === 액션 매핑 ===
const actionHandlers = {
    "추천레시피": () => {
        appendBotMessage("추천 레시피를 선택하셨군요! 🍴<br>카테고리를 골라주세요:");
        showCategoryButtons(["한식", "중식", "양식", "일식","디저트"], "추천레시피");
    },
    "추천메뉴": () => {
        appendBotMessage("오늘의 추천 메뉴를 불러올 카테고리를 선택해주세요:");
        showCategoryButtons(["한식", "중식", "양식", "일식","디저트"], "추천메뉴");
    },
    "인기레시피": () => {
        $.ajax({
            url: contextPath + "/recipe/api/recipes/popular",
            type: "GET",
            dataType: "json",
            success: function(data) {
                if (!data || data.length === 0) {
                    appendBotMessage("❌ 인기 레시피가 없습니다.");
                } else {
                    renderCards(data.slice(0, 3), "recipe");
                }
            },
            error: function() {
                appendBotMessage("❌ 인기 레시피를 불러오는 중 오류가 발생했습니다.");
            }
        });
    },
    "인기게시글": () => {
        $.ajax({
            url: contextPath + "/api/boards/popular",
            type: "GET",
            dataType: "json",
            success: function(data) {
                if (!data || data.length === 0) {
                    appendBotMessage("❌ 인기 게시글이 없습니다.");
                } else {
                    renderCards(data, "board");
                }
            },
            error: function() {
                appendBotMessage("🔒 인기 게시글은 로그인 후 이용 가능합니다.");
            }
        });
    },
    "문의하기": () => {
        appendBotMessage("문의하기 메뉴를 선택하셨습니다. 내용을 입력해주세요 ✍️");
    },
    "기타": () => {
        appendBotMessage("기타 메뉴를 선택하셨습니다.");
    }
};

// === 메시지 전송 ===
function sendMessage(text) {
    if (!text) return;
    appendUserMessage(text);

    // 레시피 검색
    if (text.includes("레시피") && !text.includes("인기")) {
        const keyword = text.replace("레시피", "").replace("알려줘", "").trim();
        $.ajax({
            url: contextPath + "/recipe/api/recipes/search",
            type: "GET",
            data: { keyword: keyword, page: 0, size: 3 },
            dataType: "json",
            success: function(data) {
                if (!data.content || data.content.length === 0) {
                    appendBotMessage(`❌ "${keyword}" 레시피를 찾을 수 없습니다.`);
                } else {
                    renderCards(data.content, "recipe");
                }
            },
            error: function() {
                appendBotMessage("❌ 레시피 검색 중 오류가 발생했습니다.");
            }
        });
        return;
    }

    // 인기 레시피
    if (text.includes("인기") && text.includes("레시피")) {
        actionHandlers["인기레시피"]();
        return;
    }

    // 게시글 검색
    if (text.includes("게시글") && !text.includes("인기")) {
        const keyword = text.replace("게시글", "").replace("알려줘", "").trim();
        $.ajax({
            url: contextPath + "/api/boards/search",
            type: "GET",
            data: { keyword: keyword, page: 0, size: 3 },
            dataType: "json",
            success: function(data) {
                if (!data.content || data.content.length === 0) {
                    appendBotMessage(`❌ "${keyword}" 관련 게시글이 없습니다.`);
                } else {
                    renderCards(data.content, "board");
                }
            },
            error: function() {
                appendBotMessage("❌ 게시글 검색 중 오류가 발생했습니다.");
            }
        });
        return;
    }

    // 인기 게시글
    if (text.includes("인기") && text.includes("게시글")) {
        actionHandlers["인기게시글"]();
        return;
    }

    // === GPT API (Flask 서버) 호출 ===
    $.ajax({
        url: "http://localhost:5000/chat",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ message: text }),
        success: function(res) {
            if (res.reply) {
                appendBotMessage(res.reply);
            } else {
                appendBotMessage("❌ GPT로부터 응답이 오지 않았습니다.");
            }
        },
        error: function(xhr, status, err) {
            console.error(err);
            appendBotMessage("❌ GPT 서버 호출 중 오류가 발생했습니다.");
        }
    });
}

// === DOM 준비되면 실행 ===
document.addEventListener("DOMContentLoaded", () => {
    chatbotBtn = document.getElementById("chatbot-btn");
    chatbotWindow = document.getElementById("chatbot-window");
    closeBtn = document.getElementById("chatbot-close-btn");
    msgBox = document.getElementById("chatbot-messages");
    userInput = document.getElementById("chatbot-user-input");
    sendBtn = document.getElementById("chatbot-send-btn");

    toggleBtn = document.getElementById("chatbot-toggle-btn");
    quickBtns = document.getElementById("chatbot-quick-buttons");

    if (chatbotBtn) {
        chatbotBtn.addEventListener("click", () => {
            chatbotWindow.style.display =
                chatbotWindow.style.display === "flex" ? "none" : "flex";
            chatbotWindow.style.flexDirection = "column";
        });
    }
    if (closeBtn) closeBtn.addEventListener("click", () => chatbotWindow.style.display = "none");

    if (sendBtn) sendBtn.addEventListener("click", () => {
        sendMessage(userInput.value.trim());
        userInput.value = "";
    });
    if (userInput) userInput.addEventListener("keydown", e => {
        if (e.key === "Enter") {
            e.preventDefault();
            sendMessage(userInput.value.trim());
            userInput.value = "";
        }
    });

    // 빠른 버튼 바인딩
    document.querySelectorAll(".quick-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            const action = btn.getAttribute("data-action");
            appendUserMessage(action);
            if (actionHandlers[action]) {
                actionHandlers[action]();
            } else {
                appendBotMessage(`"${action}"을(를) 선택하셨군요!`);
            }
        });
    });

    // ✅ 빠른 버튼 접기/펼치기
    if (toggleBtn && quickBtns) {
        toggleBtn.addEventListener("click", () => {
            if (quickBtns.classList.contains("hidden")) {
                quickBtns.classList.remove("hidden");
                toggleBtn.textContent = "▼";
            } else {
                quickBtns.classList.add("hidden");
                toggleBtn.textContent = "▲";
            }
        });
    }
});
