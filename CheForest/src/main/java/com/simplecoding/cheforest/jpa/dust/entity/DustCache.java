package com.simplecoding.cheforest.jpa.dust.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DustCache {

    @Id
    private String sido;        // 시/도 이름 (PK)

    private String pm10;        // 미세먼지
    private String pm25;        // 초미세먼지
    @Column(name = "pm10_grade")
    private String pm10Grade;   // 미세먼지 등급
    @Column(name = "pm25_grade")
    private String pm25Grade;   // 초미세먼지 등급
    @Column(name = "data_time")
    private String dataTime;    // 측정 시각

    // ✅ API 성공/실패 구분용
    @Column(name = "result_code")
    private String resultCode;  // "OK" or "EX"
}
