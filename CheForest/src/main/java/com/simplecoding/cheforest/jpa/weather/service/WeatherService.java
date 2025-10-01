// src/main/java/com/simplecoding/cheforest/jpa/weather/service/WeatherService.java
package com.simplecoding.cheforest.jpa.weather.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplecoding.cheforest.jpa.weather.entity.WeatherCache;
import com.simplecoding.cheforest.jpa.weather.repository.WeatherCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherCacheRepository weatherCacheRepository;

    // ✅ 전체 조회 (DB 캐시)
    public List<WeatherCache> findAll() {
        return weatherCacheRepository.findAll();
    }

    // ✅ 특정 지역 조회 (DB 캐시)
    public WeatherCache findByRegion(String region) {
        return weatherCacheRepository.findById(region).orElse(null);
    }
}
