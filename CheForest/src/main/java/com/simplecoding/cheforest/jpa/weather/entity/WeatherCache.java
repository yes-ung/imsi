// src/main/java/com/simplecoding/cheforest/jpa/weather/entity/WeatherCache.java
package com.simplecoding.cheforest.jpa.weather.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherCache {

    @Id
    @Column(length = 20)
    private String region;       // 지역 (PK)

    private String temperature;  // 기온
    private String humidity;     // 습도
    private String sky;          // 하늘 상태 (맑음, 흐림 등)

    @Column(name = "base_date")
    private String baseDate;     // 기준 날짜

    @Column(name = "base_time")
    private String baseTime;     // 기준 시간

    @Column(name = "data_time")
    private LocalDateTime dataTime; // 저장 시각

    @Column(name = "result_code")
    private String resultCode;   // OK / EX
}
