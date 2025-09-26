package com.simplecoding.cheforest.jpa.weather.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplecoding.cheforest.jpa.weather.dto.WeatherDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@RestController
public class WeatherController {

    private static final String SERVICE_KEY =
            "3WEYjbpIjKwt0F0YJNn1HsEtZUeiUTA%2BOTnqYz%2BHPB8X0o29U8OYJIEdTL5b4IMz0G16W1oMj6cVRNp4fnL1dA%3D%3D";

    private static final String BASE_URL =
            "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    private final ObjectMapper om = new ObjectMapper();

    // ✅ 전국 주요 지역 좌표 (nx, ny)
    private static final Map<String, int[]> REGION_COORDS = Map.ofEntries(
            Map.entry("서울", new int[]{60, 127}),
            Map.entry("부산", new int[]{98, 76}),
            Map.entry("대구", new int[]{89, 90}),
            Map.entry("인천", new int[]{55, 124}),
            Map.entry("광주", new int[]{58, 74}),
            Map.entry("대전", new int[]{67, 100}),
            Map.entry("울산", new int[]{102, 84}),
            Map.entry("세종", new int[]{66, 103}),
            Map.entry("경기", new int[]{60, 120}),
            Map.entry("강원", new int[]{73, 134}),
            Map.entry("충북", new int[]{69, 107}),
            Map.entry("충남", new int[]{68, 100}),
            Map.entry("전북", new int[]{63, 89}),
            Map.entry("전남", new int[]{51, 67}),
            Map.entry("경북", new int[]{89, 91}),
            Map.entry("경남", new int[]{91, 77}),
            Map.entry("제주", new int[]{52, 38})
    );

    /**
     * ✅ 특정 지역 날씨 조회
     */
    @GetMapping("/weather/today")
    public WeatherDto getWeather(
            @RequestParam(defaultValue = "60") int nx,
            @RequestParam(defaultValue = "127") int ny,
            @RequestParam(defaultValue = "서울") String region
    ) {
        try {
            // ====== 날짜/시간 보정 ======
            Calendar cal = Calendar.getInstance();
            String baseDate = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            // 기상청 발표시각 (02, 05, 08, 11, 14, 17, 20, 23)
            int[] baseHours = {2, 5, 8, 11, 14, 17, 20, 23};
            int selectedHour = baseHours[0];

            for (int h : baseHours) {
                if (hour >= h) {
                    selectedHour = h;
                }
            }

            // 새벽 0~1시는 전날 23시 데이터 사용
            if (hour < 2 || (hour == 2 && minute < 45)) {
                cal.add(Calendar.DATE, -1);
                baseDate = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
                selectedHour = 23;
            }

            String baseTime = String.format("%02d00", selectedHour);

            // ====== API 호출 ======
            String urlStr = BASE_URL
                    + "?serviceKey=" + SERVICE_KEY
                    + "&numOfRows=1000&pageNo=1&dataType=JSON"
                    + "&base_date=" + baseDate
                    + "&base_time=" + baseTime
                    + "&nx=" + nx
                    + "&ny=" + ny;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) sb.append(line);
            rd.close();
            conn.disconnect();

            // ====== JSON 파싱 ======
            JsonNode items = om.readTree(sb.toString())
                    .path("response").path("body").path("items").path("item");

            String temp = "";
            String hum = "";
            String sky = "";

            if (items.isArray()) {
                for (JsonNode it : items) {
                    String category = it.path("category").asText();
                    String fcstValue = it.path("fcstValue").asText();

                    switch (category) {
                        case "TMP": temp = fcstValue + "℃"; break;
                        case "REH": hum = fcstValue + "%"; break;
                        case "SKY":
                            switch (fcstValue) {
                                case "1": sky = "맑음"; break;
                                case "3": sky = "구름많음"; break;
                                case "4": sky = "흐림"; break;
                            }
                            break;
                        case "PTY": // PTY가 SKY보다 우선
                            switch (fcstValue) {
                                case "1": sky = "비"; break;
                                case "2": sky = "비/눈"; break;
                                case "3": sky = "눈"; break;
                                case "5": sky = "빗방울"; break;
                                case "6": sky = "빗방울눈날림"; break;
                                case "7": sky = "눈날림"; break;
                            }
                            break;
                    }
                }
            }

            return new WeatherDto(region,
                    temp.isEmpty() ? "-" : temp,
                    hum.isEmpty() ? "-" : hum,
                    sky.isEmpty() ? "-" : sky,
                    baseDate, baseTime);

        } catch (Exception e) {
            return new WeatherDto(region, "-", "-", "ERROR: " + e.getMessage(), "-", "-");
        }
    }

    /**
     * ✅ 전국 주요 지역 날씨 조회
     */
    @GetMapping("/weather/today/all")
    public List<WeatherDto> getAllWeather() {
        List<WeatherDto> results = new ArrayList<>();

        for (Map.Entry<String, int[]> entry : REGION_COORDS.entrySet()) {
            String region = entry.getKey();
            int[] coords = entry.getValue();

            WeatherDto dto = getWeather(coords[0], coords[1], region);
            results.add(dto);
        }

        return results;
    }
}
