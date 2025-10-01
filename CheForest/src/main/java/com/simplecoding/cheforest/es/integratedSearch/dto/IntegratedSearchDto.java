package com.simplecoding.cheforest.es.integratedSearch.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IntegratedSearchDto {
    private String id;
    private String type;         // (recipe, board 구분)
    private String title;        //  요리명
    private String thumbnail;    // 요리사진URL
    private String ingredients;  // 재료
    private String category;     //한식,중식, 양식
}