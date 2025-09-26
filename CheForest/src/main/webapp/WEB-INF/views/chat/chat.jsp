<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>chat</title>
  <link rel="stylesheet" href="/css/chat.css">
</head>
<body>

  <div class="chat-container">
    <header class="chat-header">
      <p>와글와글 요리이야기</p>
    </header>


    <div id="chatBox" class="chatBox">
    </div>

    <footer class="chat-input-area">
      <input type="text" id="msgInput" placeholder="메시지를 입력하세요..." onkeydown="handleKey(event)">
    </footer>

  </div>

  <!-- 이모티콘 버튼 영역 -->
  <div id="emojiBox"
       style="margin-top:5px; display:flex; gap:5px; align-items:center; flex-wrap:wrap;">
  </div>

<%--++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++--%>

  <sec:authentication property="principal.memberIdx" var="currentMemberIdx" />

<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>

  <script>
    const MY_ID = Number("${currentMemberIdx != null ? currentMemberIdx : 0}");
  </script>

  <script src="/js/chat.js"></script>

</body>
</html>