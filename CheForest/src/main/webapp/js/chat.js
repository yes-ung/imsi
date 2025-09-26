// let : 변수
// const : 상수
// function : 함수
// document : html 접근 (dom)
// async : 비동기 처리

// 하드코딩된 이모티콘 리스트
const emojiList = [
    "/emoji/hi.png",
    "/emoji/good.png",
    "/emoji/yes.png",
    "/emoji/no.png",
    "/emoji/cheer up.png",
    "/emoji/delicious.png",
    "/emoji/not tasty.png",
    "/emoji/oh no.png",
    "/emoji/recommen.png",
    "/emoji/sad.png",
    "/emoji/wow.png",
    // 추후 개선
];

let stompClient = null;
// 연결
function connect() {
    const socket = new SockJS("/ws")
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        // 구독
        stompClient.subscribe("/sub/message", (msg) => {
            // 🌟🌟🌟 이 부분이 핵심입니다! 🌟🌟🌟
            console.log("--- 서버로부터 수신된 원본 메시지 (msg.body) ---");
            console.log(msg.body);

            const chat = JSON.parse(msg.body);

            console.log("--- JSON 파싱 후 chat 객체 ---");
            console.log(chat);
            // 🌟🌟🌟 닉네임과 함께 senderId가 오는지 여기서 확인해야 합니다. 🌟🌟🌟
            showMessage(chat)
        })

        loadChatHistory();
    });
}

// 이전 채팅 불러오기
async function loadChatHistory() {
    const res = await fetch("/chat/history");
    const messages = await res.json();
    messages.forEach(chat => {
        showMessage(chat);
    });
}

// 출력
function showMessage(chat) {
    const chatBox = document.getElementById("chatBox");

    // 🌟 MY_ID와 chat.senderId를 모두 콘솔에 출력합니다. 🌟
    console.log("--------------------------------------");
    console.log("MY_ID (현재 유저 ID):", MY_ID);
    console.log("SenderId (수신 메시지 ID):", chat.senderId);
    console.log("--------------------------------------");

    // 1. **PK(ID)를 사용하여 현재 사용자가 보낸 메시지인지 확인**
    //    서버에서 senderId를 숫자로 받고, MY_ID도 숫자로 변환하여 비교하는 것이 가장 안전합니다.
    const isSentByCurrentUser = Number(chat.senderId) === Number(MY_ID);

    // 2. 메시지 유형에 따른 CSS 클래스 이름 설정
    const rowClassName = isSentByCurrentUser ? "message-message-my" : "message-message";
    const msgBubbleClassName = isSentByCurrentUser ? "message-my" : "message";

    // 3. 메시지 전체를 감쌀 행(row) 컨테이너 생성 (CSS로 가로 정렬 담당)
    const rowDiv = document.createElement("div");
    rowDiv.classList.add("message-row", rowClassName);

    // A. 프로필 이미지 컨테이너
    const profileImgDiv = document.createElement("div");
    profileImgDiv.classList.add("message-profile-img");
    const img = document.createElement("img");
    img.src = chat.profile || "/image/default.png";
    img.width = 40; img.height = 40; img.style.borderRadius = "50%";
    profileImgDiv.appendChild(img);

    // B. 말풍선과 닉네임을 담을 컨테이너 (CSS로 수직 정렬 담당)
    const contentWrapper = document.createElement("div");
    contentWrapper.classList.add("message-content-wrapper");

    // 닉네임 영역 (말풍선 위에 위치)
    const senderName = document.createElement("div");
    senderName.classList.add("message-sender-name");
    senderName.innerHTML = `<strong>${chat.sender}</strong>`;

    // 🌟 1. 닉네임 추가 순서 수정: 상대방 메시지일 경우 닉네임을 가장 먼저 contentWrapper에 추가합니다. 🌟
    if (!isSentByCurrentUser) {
        contentWrapper.appendChild(senderName); // (닉네임이 첫 번째 요소)
    }

    // 실제 말풍선 버블
    const msgBubble = document.createElement("div");
    msgBubble.classList.add("message-bubble", msgBubbleClassName);

    // 메시지 내용 (텍스트 또는 이모티콘)
    if (chat.message.startsWith("/emoji/")) {
        msgBubble.innerHTML = `<img src='${chat.message}' width='100' height='100'>`;
    } else {
        msgBubble.textContent = chat.message;
    }
    contentWrapper.appendChild(msgBubble); // (말풍선이 두 번째 요소)

    // C. 전송 시간 영역
    const timeDiv = document.createElement("div");
    timeDiv.classList.add("message-time");
    timeDiv.textContent = chat.time; // 예: "3분 전"

    // ** 상대방 메시지 (좌측 정렬): 프로필, 닉네임(위에), 버블, 시간 모두 표시 **
    if (!isSentByCurrentUser) {
        // [프로필] [닉네임+버블] [시간] 순서

        // 🌟 2. 메시지 조립 시, 여기서 닉네임을 다시 추가하지 않습니다. (중복 방지)

        rowDiv.appendChild(profileImgDiv);
        rowDiv.appendChild(contentWrapper);
        rowDiv.appendChild(timeDiv);
    }

    // ** 내 메시지 (우측 정렬): 버블과 시간만 표시 **
    else {
        // 1. 버블 래퍼 정렬 설정
        contentWrapper.style.alignItems = 'flex-end';

        // 2. 시간과 버블을 묶을 래퍼 생성
        const bubbleAndInfoWrapper = document.createElement("div");
        bubbleAndInfoWrapper.classList.add("my-bubble-info-wrapper");

        // 3. 시간과 말풍선을 래퍼에 추가 (시간, 버블 순서)
        bubbleAndInfoWrapper.appendChild(timeDiv);
        bubbleAndInfoWrapper.appendChild(contentWrapper);

        // 4. 최종 행(rowDiv)에 추가
        rowDiv.appendChild(bubbleAndInfoWrapper);
    }

    // 최종적으로 전체 행을 채팅창에 추가
    chatBox.appendChild(rowDiv);
    chatBox.scrollTop = chatBox.scrollHeight;
}

// 발행
function sendMessage() {
    const input = document.getElementById("msgInput").value.trim();

    if (!input) {
        alert("메시지를 입력하세요.");
        return;
    }

    const message = {
        type: "TEXT",
        message: input
    };
    stompClient.send("/pub/message", {}, JSON.stringify(message));
    document.getElementById("msgInput").value = "";
}

// 이모티콘 전송
function sendEmoji(url) {
    const message = {
        type: "IMAGE",
        message: url
    };
    stompClient.send("/pub/message", {}, JSON.stringify(message));
}

// 이모티콘 버튼 생성
function loadEmojis() {
    const emojiBox = document.getElementById("emojiBox");
    emojiList.forEach(url => {
        const btn = document.createElement("button");
        btn.style.border = "none";
        btn.style.background = "transparent";
        btn.style.cursor = "pointer";
        btn.style.padding = "2px";

        btn.innerHTML = '<img src="' + url + '" width="75" height="75" />';
        btn.onclick = () => sendEmoji(url);
        emojiBox.appendChild(btn);
    });
}

// 엔터키로 전송
function handleKey(event) {
    if (event.key === "Enter") {
        sendMessage();
        event.preventDefault();
    }
}

connect();
loadEmojis();