<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <!-- jQuery CDN -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <meta charset="UTF-8">
    <title>셰프봇</title>

    <style>
        /* === 챗봇 버튼 === */
        #chatbot-btn {
            position: fixed;
            bottom: 20px;
            right: 20px;
            width: 70px;
            height: 70px;
            border-radius: 50%;
            background: #4a90e2;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            box-shadow: 0 4px 12px rgba(0,0,0,.25);
            z-index: 9999;
            color: #fff;
            font-weight: bold;
            font-size: 18px;
        }

        /* === 챗봇 창 === */
        #chatbot-window {
            display: none;
            position: fixed;
            bottom: 100px;
            right: 20px;
            width: 320px;
            height: 500px;
            border-radius: 12px;
            background: #fff;
            box-shadow: 0 4px 15px rgba(0,0,0,.3);
            overflow: hidden;
            z-index: 10000;
            display: flex;
            flex-direction: column;
            font-family: "Noto Sans KR", sans-serif;
        }

        /* === 헤더 === */
        .chat-header {
            background: #4a90e2;
            color: #fff;
            padding: 10px 14px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-weight: bold;
            font-size: 15px;
        }
        .chat-header-left {
            display: flex;
            align-items: center;
            gap: 6px;
        }
        .chat-header-left img {
            width: 20px;
            height: 20px;
        }
        .chat-header button {
            background: none;
            border: none;
            color: #fff;
            font-size: 18px;
            cursor: pointer;
        }

        /* === 메시지 영역 === */
        #chatbot-messages {
            flex: 1;
            padding: 10px;
            overflow-y: auto;
            font-size: 14px;
            background: #fafafa;
        }
        .message { display: flex; margin: 6px 0; }
        .user-msg { justify-content: flex-end; }
        .bot-msg {
            justify-content: flex-start;
            display: flex;
            align-items: flex-start;
        }

        .bot-avatar {
            width: 30px;
            height: 30px;
            border-radius: 50%;
            flex-shrink: 0;
            margin-right: 6px;
        }
        .bubble {
            max-width: 70%;
            padding: 8px 12px;
            border-radius: 16px;
            line-height: 1.4;
            font-size: 14px;
            word-break: break-word;
            white-space: pre-wrap;
        }
        .bot-msg .bubble {
            background: #f1f1f1;
            color: #333;
            border-bottom-left-radius: 4px;
        }
        .user-msg .bubble {
            background: #4a90e2;
            color: #fff;
            border-bottom-right-radius: 4px;
        }

        /* === 토글 화살표 === */
        #chatbot-toggle-btn {
            text-align: center;
            font-size: 14px;
            color: #4a90e2;
            cursor: pointer;
            padding: 4px 0;
            background: #f5f5f5;
            border-top: 1px solid #ddd;
            user-select: none;
        }
        #chatbot-toggle-btn:hover { background: #eef6ff; }

        /* === 빠른 버튼 (슬라이드 애니메이션 적용) === */
        #chatbot-quick-buttons {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 6px;
            padding: 8px;
            background: #f5f5f5;
            border-top: 1px solid #ddd;

            max-height: 300px;
            overflow: hidden;
            transition: max-height 0.2s ease, padding 0.2s ease;
        }
        #chatbot-quick-buttons.hidden {
            max-height: 0;
            padding: 0;
            border: none;
        }

        .quick-btn {
            padding: 6px;
            font-size: 13px;
            border: 1px solid #ccc;
            border-radius: 6px;
            background: #fff;
            cursor: pointer;
        }
        .quick-btn:hover {
            background: #e9f7f0;
            border-color: #4a90e2;
            color: #4a90e2;
        }

        /* === 입력창 === */
        #chatbot-input {
            display: flex;
            border-top: 1px solid #ddd;
            padding: 8px;
            background: #fff;
        }
        #chatbot-user-input {
            flex: 1;
            border: 1px solid #ddd;
            border-radius: 20px;
            padding: 8px 12px;
            font-size: 14px;
            outline: none;
        }
        #chatbot-send-btn {
            border: none;
            background: #4a90e2;
            color: #fff;
            width: 40px;
            height: 40px;
            border-radius: 50%;
            margin-left: 6px;
            cursor: pointer;
            font-size: 16px;
        }
    </style>
</head>
<body>
<!-- 챗봇 버튼 -->
<div id="chatbot-btn">AI</div>

<!-- 챗봇 창 -->
<div id="chatbot-window">
    <div class="chat-header">
        <div class="chat-header-left">
            <img src="<%=request.getContextPath()%>/images/bear-mascot.png" alt="셰프봇">
            <span>셰프봇</span>
        </div>
        <button id="chatbot-close-btn">✕</button>
    </div>
    <div id="chatbot-messages">
        <!-- 초기 봇 메시지 -->
        <div class="message bot-msg">
            <img src="<%=request.getContextPath()%>/images/bear-mascot.png" alt="셰프봇" class="bot-avatar">
            <div class="bubble">안녕하세요! 저는 셰프봇이에요 🍳</div>
        </div>
    </div>

    <!-- ✅ 토글 화살표 (기본 ▼) -->
    <div id="chatbot-toggle-btn">▼</div>

    <!-- 빠른 버튼 -->
    <div id="chatbot-quick-buttons">
        <button type="button" class="quick-btn" data-action="추천레시피">추천 레시피</button>
        <button type="button" class="quick-btn" data-action="추천메뉴">추천 메뉴</button>
        <button type="button" class="quick-btn" data-action="인기레시피">인기 레시피</button>
        <button type="button" class="quick-btn" data-action="인기게시글">인기 게시글</button>
        <button type="button" class="quick-btn" data-action="문의하기">문의하기</button>
        <button type="button" class="quick-btn" data-action="기타">기타</button>
    </div>

    <!-- 입력창 -->
    <div id="chatbot-input">
        <input type="text" id="chatbot-user-input" placeholder="메시지를 입력하세요">
        <button id="chatbot-send-btn">▶</button>
    </div>
</div>

<!-- ✅ JS 분리 -->
<script defer src="<%=request.getContextPath()%>/js/chatbot.js"></script>
</body>
</html>
