<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>오늘의 날씨 & 미세먼지 위젯</title>
    <link rel="stylesheet" href="/css/dust.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<div class="container">
    <div class="widget" id="widget">
        <div class="header">
            <button id="prev">&#9664;</button>
            <span id="region" class="region">-</span>
            <button id="next">&#9654;</button>
        </div>

        <!-- 날씨 -->
        <div id="temp" class="temp">--°C</div>
        <div id="desc" class="desc">날씨: -</div>

        <!-- 미세먼지 -->
        <div class="dust">
            <p id="pm10">미세먼지: -</p>
            <p id="pm25">초미세먼지: -</p>
        </div>

        <!-- 날짜/시간 -->
        <div class="datetime">
            <span id="date">--</span> <span id="time">--:--</span>
        </div>
    </div>

    <!-- 추천 음식 -->
    <div id="recommend" class="recommend">
        <p>미세먼지에 좋은 음식</p>
        <ul>
            <li><a href="/recipe/detail?name=도라지배즙">도라지배즙</a></li>
            <li><a href="/recipe/detail?name=녹차">녹차</a></li>
            <li><a href="/recipe/detail?name=미나리무침">미나리무침</a></li>
        </ul>
    </div>
</div>

<script>
    let dustData = [];
    let weatherData = [];   // 전국 날씨 데이터를 한 번만 저장
    let currentIndex = 0;

    function updateCard() {
        if (weatherData.length === 0) return;

        let regionWeather = weatherData[currentIndex];
        let region = regionWeather.region;

        // 지역 표시
        $("#region").text(region);

        // 날씨 표시
        $("#temp").text(regionWeather.temperature);
        $("#desc").text("날씨: " + regionWeather.sky + " (습도 " + regionWeather.humidity + ")");

        // 미세먼지 데이터 찾기
        let dust = dustData.find(d => d.sido.includes(region));
        if (dust) {
            $("#pm10").text("미세먼지: " + dust.pm10 + " (" + dust.pm10Grade + ")");
            $("#pm25").text("초미세먼지: " + dust.pm25 + " (" + dust.pm25Grade + ")");

            // 등급별 배경색 적용
            let widget = $("#widget");
            widget.removeClass("good normal bad very-bad unknown");
            let grade = dust.pm25Grade !== "정보없음" ? dust.pm25Grade : dust.pm10Grade;
            switch (grade) {
                case "좋음": widget.addClass("good"); break;
                case "보통": widget.addClass("normal"); break;
                case "나쁨": widget.addClass("bad"); break;
                case "매우나쁨": widget.addClass("very-bad"); break;
                default: widget.addClass("unknown");
            }

            // 추천 음식 표시 여부
            if (grade.includes("나쁨")) {
                $("#recommend").slideDown(200);
            } else {
                $("#recommend").slideUp(200);
            }
        }
    }

    // 시계
    function updateClock() {
        const now = new Date();
        $("#date").text(now.toLocaleDateString("ko-KR", { month: "long", day: "numeric", weekday: "long" }));
        $("#time").text(now.toLocaleTimeString("ko-KR", { hour: "2-digit", minute: "2-digit" }));
    }
    setInterval(updateClock, 1000);
    updateClock();

    $(document).ready(function() {
        // ✅ 전국 날씨 한 번만 가져오기
        $.getJSON("/weather/today/all", function(data) {
            weatherData = data;   // 배열 저장
            updateCard();         // 첫 화면 출력
        });

        // ✅ 전국 미세먼지 데이터 한 번만 가져오기
        $.getJSON("/dust/today-all", function(data) {
            dustData = data;
            updateCard();
        });

        // 버튼 이벤트
        $("#prev").click(function() {
            currentIndex = (currentIndex - 1 + weatherData.length) % weatherData.length;
            updateCard();
        });

        $("#next").click(function() {
            currentIndex = (currentIndex + 1) % weatherData.length;
            updateCard();
        });
    });
</script>
</body>
</html>
