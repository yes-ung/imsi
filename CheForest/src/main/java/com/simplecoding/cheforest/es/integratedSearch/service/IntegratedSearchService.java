package com.simplecoding.cheforest.es.integratedSearch.service;



import com.simplecoding.cheforest.es.integratedSearch.dto.IntegratedSearchDto;
import com.simplecoding.cheforest.es.integratedSearch.entity.IntegratedSearch;
import com.simplecoding.cheforest.jpa.common.MapStruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IntegratedSearchService {
    private final ElasticsearchOperations elasticsearchOperations; // TODO: querydsl 작성용 클래스
    private final MapStruct mapStruct;
// 검색기능
    public Page<IntegratedSearchDto> search(String keyword, Pageable pageable) {
        Query query = new NativeQueryBuilder()
                .withQuery(
                        q -> q.multiMatch(
                                m -> m.fields("title", "category", "ingredients")
                                        .query(keyword)
//                                        .fuzziness("AUTO")  // 오타 자동 보정(너무 부정확한것들 까지 검색되서 일단 주석처리)
                        )
                )
                .withPageable(pageable)
                .build();

        SearchHits<IntegratedSearch> hits = elasticsearchOperations.search(query, IntegratedSearch.class);

        List<IntegratedSearchDto> content = hits.getSearchHits().stream()
                .map(h -> {
                    IntegratedSearchDto dto = mapStruct.toDto(h.getContent());
                    return dto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, hits.getTotalHits());
    }
    
    
//     수정, 추가
    public void saveData(IntegratedSearch integratedSearch) {
        IntegratedSearch entity = new IntegratedSearch();
        entity.setId("board-" + integratedSearch.getId());
        entity.setTitle(integratedSearch.getTitle());
        entity.setCategory(integratedSearch.getCategory());
        entity.setIngredients(integratedSearch.getIngredients());
        entity.setType(integratedSearch.getType());
        entity.setThumbnail(integratedSearch.getThumbnail());

        elasticsearchOperations.save(entity);  // 추가 또는 수정
    }
//      삭제
    public void deleteData(String id) {
        elasticsearchOperations.delete("board-" + id, IntegratedSearch.class);  // 삭제
    }





















}
