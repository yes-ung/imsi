package com.simplecoding.cheforest.es.integratedSearch.controller;



import com.simplecoding.cheforest.es.integratedSearch.dto.IntegratedSearchDto;
import com.simplecoding.cheforest.es.integratedSearch.service.IntegratedSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class IntegratedSearchController {
    private final IntegratedSearchService integratedSearchService;

    //    통합검색
    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "") String totalKeyword,
                         @PageableDefault(page = 0, size = 3) Pageable pageable,
                         Model model) {
//        TODO: 1) 서비스의 search 메소드 실행
        Page<IntegratedSearchDto> pages = integratedSearchService.search(totalKeyword, pageable);
        model.addAttribute("totalKeyword", totalKeyword);
        model.addAttribute("pages", pages);
        model.addAttribute("searches", pages.getContent()); // 결과 배열
        return "search/searchAll";                                           // jsp(통합검색 결과페이지)
    }
}
