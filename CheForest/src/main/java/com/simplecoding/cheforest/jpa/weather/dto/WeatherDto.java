// src/main/java/com/simplecoding/simpledms/weather/dto/WeatherDto.java
package com.simplecoding.cheforest.jpa.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {
    private String region;
    private String temperature;
    private String humidity;
    private String sky;
    private String baseDate;
    private String baseTime;
}
