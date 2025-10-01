function showResult(region) {
    const resultBox = document.getElementById("result-box");

    // 초기 로딩 메시지
    resultBox.innerHTML = `
        <h3>${region}</h3>
        <p>데이터 불러오는 중...</p>
    `;

    // API 호출
    fetch(`/dust/with-recommend?sido=${region}`)
        .then(res => res.json())
        .then(data => {
            // ✅ 미세먼지/날씨 출력
            resultBox.innerHTML = `
              <h3>${region}</h3>
              <p>기온: ${data.weather?.temperature || "-"}</p>
              <p>습도: ${data.weather?.humidity || "-"}</p>
              <p>하늘: ${data.weather?.sky || "-"}</p>
              <p>미세먼지(PM10): ${data.pm10 || "-"} (${data.pm10Grade || "정보없음"})</p>
              <p>초미세먼지(PM2.5): ${data.pm25 || "-"} (${data.pm25Grade || "정보없음"})</p>
              <p>측정시간: ${data.dataTime || "-"}</p>

              <!-- ✅ 레시피 박스 -->
              <div id="recipe-box" class="recipe-box">
                  <h4 id="recipe-header">추천 레시피 ▼</h4>
                  <ul id="recipe-list"></ul>
              </div>
            `;

            // ✅ 새로 그려진 DOM에서 다시 요소 선택
            const recipeListEl = document.getElementById("recipe-list");
            const recipeBoxEl = document.getElementById("recipe-box");

            // ✅ 레시피 목록 처리
            const recipes = data.recipes || [];
            if (recipes.length > 0) {
                recipeListEl.innerHTML = recipes
                    .map(r => `<li>${r.titleKr || r.title || "레시피"}</li>`)
                    .join("");
                recipeBoxEl.style.display = "block"; // 표시
            } else {
                recipeBoxEl.style.display = "none"; // 숨김
            }
        })
        .catch(err => {
            console.error(err);
            resultBox.innerHTML = `
              <h3>${region}</h3>
              <p>데이터 불러오기 오류</p>
            `;
        });
}
