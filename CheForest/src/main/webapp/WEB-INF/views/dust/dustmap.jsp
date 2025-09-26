<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>대한민국 미세먼지 & 레시피 추천 지도</title>
  <style>
    body { font-family: Arial, sans-serif; text-align: center; }
    .map-container {
      position: relative;
      display: inline-block;
    }
    .map-container img {
      width: 800px;
      height: auto;
      display: block;
    }
    .region-btn {
      position: absolute;
      z-index: 10;
      padding: 3px 6px;
      font-size: 11px;
      border: 1px solid #aaa;
      border-radius: 4px;
      background: rgba(255,255,255,0.9);
      cursor: pointer; /* ✅ 클릭 모드 */
      box-shadow: 0 0 2px rgba(0,0,0,0.3);
    }
    #result-box {
      position: absolute;
      top: 100px;
      left: 880px; /* 지도 오른쪽 */
      width: 280px;
      min-height: 150px;
      padding: 12px;
      border: 2px solid #333;
      background: #fff;
      text-align: left;
    }
  </style>
</head>
<body>
<h2>대한민국 지역별 미세먼지 & 레시피 추천</h2>

<div class="map-container">
  <img src="/images/korea_map.png" alt="대한민국 지도">

  <!-- ✅ 고정된 좌표 (드래그 불필요) -->
  <button class="region-btn" style="top:182px; left:285px;" onclick="showResult('서울')">서울</button>
  <button class="region-btn" style="top:140px; left:320px;" onclick="showResult('경기')">경기</button>
  <button class="region-btn" style="top:190px; left:245px;" onclick="showResult('인천')">인천</button>
  <button class="region-btn" style="top:159px; left:407px;" onclick="showResult('강원')">강원</button>

  <button class="region-btn" style="top:260px; left:350px;" onclick="showResult('충북')">충북</button>
  <button class="region-btn" style="top:275px; left:251px;" onclick="showResult('충남')">충남</button>
  <button class="region-btn" style="top:292px; left:307px;" onclick="showResult('세종')">세종</button>
  <button class="region-btn" style="top:317px; left:321px;" onclick="showResult('대전')">대전</button>

  <button class="region-btn" style="top:378px; left:297px;" onclick="showResult('전북')">전북</button>
  <button class="region-btn" style="top:447px; left:270px;" onclick="showResult('광주')">광주</button>
  <button class="region-btn" style="top:484px; left:272px;" onclick="showResult('전남')">전남</button>

  <button class="region-btn" style="top:295px; left:446px;" onclick="showResult('경북')">경북</button>
  <button class="region-btn" style="top:367px; left:428px;" onclick="showResult('대구')">대구</button>
  <button class="region-btn" style="top:425px; left:392px;" onclick="showResult('경남')">경남</button>
  <button class="region-btn" style="top:446px; left:472px;" onclick="showResult('부산')">부산</button>
  <button class="region-btn" style="top:398px; left:485px;" onclick="showResult('울산')">울산</button>

  <button class="region-btn" style="top:636px; left:247px;" onclick="showResult('제주')">제주</button>

  <!-- 결과 박스 -->
  <div id="result-box">
    <b>지역을 선택하세요</b>
  </div>
</div>

<script>
  function showResult(region) {
    const resultBox = document.getElementById("result-box");

    // ✅ 스프링 API 호출 (예시: /dust/with-recommend?sido=서울)
    fetch(`/dust/with-recommend?sido=${region}`)
            .then(res => res.json())
            .then(data => {
              const dustInfo = `
                <p><b>${region}</b></p>
                <p>PM10: ${data.dust.pm10} (${data.dust.pm10Grade})</p>
                <p>PM2.5: ${data.dust.pm25} (${data.dust.pm25Grade})</p>
            `;
              let recipes = data.recipes;
              try { recipes = JSON.parse(data.recipes); } catch(e){}
              let recipeHtml = "<h4>추천 레시피</h4><ul>";
              (recipes||[]).forEach(r => { recipeHtml += `<li>${r.title||r.TITLE}</li>`; });
              recipeHtml += "</ul>";
              resultBox.innerHTML = dustInfo + recipeHtml;
            })
            .catch(err => {
              console.error(err);
              resultBox.innerHTML = "<p>데이터 불러오기 오류</p>";
            });
  }
</script>
</body>
</html>
