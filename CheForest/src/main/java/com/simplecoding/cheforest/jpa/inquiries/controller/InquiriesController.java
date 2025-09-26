package com.simplecoding.cheforest.jpa.inquiries.controller;


import com.simplecoding.cheforest.jpa.inquiries.dto.InquiryWithNicknameDto;
import com.simplecoding.cheforest.jpa.inquiries.entity.Inquiries;
import com.simplecoding.cheforest.jpa.inquiries.repository.InquiriesRepository;
import com.simplecoding.cheforest.jpa.inquiries.service.InquiriesService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Log4j2
@RequiredArgsConstructor
@RestController
public class InquiriesController {

    private final InquiriesService inquiriesService;
    private final InquiriesRepository inquiriesRepository;


    @GetMapping("/api/inquiries")
    public Map<String, Object> getPagedInquiries(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<InquiryWithNicknameDto> pageResult = inquiriesService.findInquiryWithNickname(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("data", pageResult.getContent());       // 실제 문의 내역
        response.put("total", pageResult.getTotalElements()); // 전체 개수
        response.put("totalPages", pageResult.getTotalPages()); // 전체 페이지 수
        response.put("page", pageResult.getNumber() + 1);     // 현재 페이지 번호 (1-based)
        return response;
    }

    @GetMapping("/api/inquiries/countAllInquiries")
    public Long countAllInquiries() {
        return inquiriesService.countAllInquiries();
    }

    @GetMapping("/api/inquiries/countPendingInquiries")
    public Long countPendingInquiries() {
        return inquiriesService.countPendingInquiries();
    }
    @GetMapping("/api/inquiries/countAnsweredInquiries")
    public Long countAnsweredInquiries() {
        return inquiriesService.countAnsweredInquiries();
    }
    @GetMapping("/api/inquiries/countTodayInquiries")
    public Long countTodayInquiries() {
        return inquiriesService.countTodayInquiries();
    }

    @GetMapping("/api/inquiries/pending")
    public List<InquiryWithNicknameDto> findPendingInquiriesWithNickname() {
        return inquiriesService.findPendingInquiriesWithNickname();
    }
    @PostMapping("/inquiries/answer")
    public ResponseEntity<String> submitAnswer(@RequestBody AnswerRequestDto dto) {
        try {
            log.info("답변 요청 수신: {}", dto);

            Optional<Inquiries> optional = inquiriesRepository.findById(dto.getInquiryId());
            if (optional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("문의 내역을 찾을 수 없습니다.");
            }

            Inquiries inquiry = optional.get();
            inquiry.setAnswerContent(dto.getAnswerContent());
            inquiry.setAnswerStatus("답변완료");
            inquiry.setAnswerAt(new Date());

            inquiriesRepository.save(inquiry);

            return ResponseEntity.ok("답변 저장 완료");

        } catch (Exception e) {
            log.error("답변 저장 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생: " + e.getMessage());
        }
    }


    @Getter
    @Setter
    public static class AnswerRequestDto {
        private Long inquiryId;
        private String answerContent;
    }








}
