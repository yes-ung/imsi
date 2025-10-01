package com.simplecoding.cheforest.jpa.dust.dto;

import com.simplecoding.cheforest.jpa.weather.dto.WeatherDto;
import com.simplecoding.cheforest.jpa.recipe.dto.RecipeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter   // ✅ Setter 추가
@NoArgsConstructor  // ✅ 기본 생성자
public class DustDto {
    private String sido;
    private String pm10;
    private String pm10Grade;
    private String pm25;
    private String pm25Grade;
    private String dataTime;
    private String resultCode;
    private String resultMsg;
    private String url;
    private String raw;

    // ✅ 날씨 + 레시피 확장
    private WeatherDto weather;
    private List<RecipeDto> recipes;
}
