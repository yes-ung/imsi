package com.simplecoding.cheforest.jpa.dust.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DustPingController {

    // ✅ 반드시 Encoding Key (%2B, %3D 포함 버전)
    private static final String SERVICE_KEY_ENCODING =
            "3WEYjbpIjKwt0F0YJNn1HsEtZUeiUTA%2BOTnqYz%2BHPB8X0o29U8OYJIEdTL5b4IMz0G16W1oMj6cVRNp4fnL1dA%3D%3D";

    private static final String BASE_URL =
            "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";

    private static final String[] SIDO_NAMES = {
            "서울","부산","대구","인천","광주","대전","울산","세종","경기","강원",
            "충북","충남","전북","전남","경북","경남","제주"
    };

    private final ObjectMapper om = new ObjectMapper();

    @Getter @NoArgsConstructor @AllArgsConstructor
    static class DustDto {
        private String sido;
        private String pm10;       private String pm10Grade;
        private String pm25;       private String pm25Grade;
        private String dataTime;
        private String resultCode;
        private String resultMsg;
        private String url;
        private String raw;
    }

    // ✅ 등급 변환
    private String grade(String val, boolean pm25) {
        if (val == null || val.isBlank() || "-".equals(val)) return "정보없음";
        try {
            int v = Integer.parseInt(val);
            if (pm25) return v <= 15 ? "좋음" : v <= 35 ? "보통" : v <= 75 ? "나쁨" : "매우나쁨";
            return v <= 30 ? "좋음" : v <= 80 ? "보통" : v <= 150 ? "나쁨" : "매우나쁨";
        } catch (Exception ignored) { return "정보없음"; }
    }

    // ✅ 평균 계산
    private String avg(List<String> vals) {
        int sum = 0, n = 0;
        for (String s : vals) {
            if (s != null && !s.isBlank() && !"-".equals(s)) {
                try { sum += Integer.parseInt(s); n++; } catch (Exception ignored) {}
            }
        }
        return n == 0 ? "-" : String.valueOf(Math.round((double) sum / n));
    }

    // ✅ 시도별 오늘의 미세먼지 현황
    @GetMapping("/dust/today-all")
    public List<DustDto> getTodayAll() {
        List<DustDto> out = new ArrayList<>();
        for (String sido : SIDO_NAMES) {
            out.add(fetchOnce(sido));
        }
        return out;
    }

    private DustDto fetchOnce(String sido) {
        String finalUrl = null;
        try {
            // sidoName 인코딩
            String encodedSido = URLEncoder.encode(sido, StandardCharsets.UTF_8);

            // 최종 URL (serviceKey는 인코딩 키 그대로 붙이기)
            finalUrl = BASE_URL
                    + "?serviceKey=" + SERVICE_KEY_ENCODING
                    + "&returnType=json"
                    + "&numOfRows=1000"
                    + "&pageNo=1"
                    + "&sidoName=" + encodedSido
                    + "&ver=1.0";

            // HttpURLConnection 방식
            URL url = new URL(finalUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            int code = conn.getResponseCode();
            BufferedReader rd;
            if (code >= 200 && code <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            String body = sb.toString().trim();
            if (body.isEmpty() || body.charAt(0) != '{') {
                return new DustDto(sido, "-", "정보없음", "-", "정보없음", "-",
                        "EX", "Non-JSON", finalUrl, snippet(body));
            }

            JsonNode resp = om.readTree(body).path("response");
            String resultCode = resp.path("header").path("resultCode").asText("");
            String resultMsg  = resp.path("header").path("resultMsg").asText("");

            if (!"00".equals(resultCode)) {
                return new DustDto(sido, "-", "정보없음", "-", "정보없음", "-",
                        resultCode, resultMsg, finalUrl, snippet(body));
            }

            JsonNode items = resp.path("body").path("items");
            List<String> p10 = new ArrayList<>(), p25 = new ArrayList<>();
            String dataTime = "-";
            if (items.isArray() && items.size() > 0) {
                for (JsonNode it : items) {
                    p10.add(it.path("pm10Value").asText("-"));
                    p25.add(it.path("pm25Value").asText("-"));
                    if ("-".equals(dataTime)) dataTime = it.path("dataTime").asText("-");
                }
            }

            String pm10 = avg(p10), pm25 = avg(p25);
            return new DustDto(sido, pm10, grade(pm10, false), pm25, grade(pm25, true),
                    dataTime, resultCode, resultMsg, finalUrl, null);

        } catch (Exception e) {
            return new DustDto(sido, "-", "정보없음", "-", "정보없음", "-",
                    "EX", e.getMessage(), finalUrl, null);
        }
    }

    private String snippet(String s) {
        if (s == null) return null;
        String t = s.replaceAll("\\s+", " ");
        return t.length() > 200 ? t.substring(0, 200) + "..." : t;
    }
}
