package com.simplecoding.cheforest.jpa.chatbot.controller;

import com.simplecoding.cheforest.jpa.chatbot.dto.ChatbotFaqDto;
import com.simplecoding.cheforest.jpa.chatbot.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotRestController {

    private final ChatbotService chatbotService;

    // ✅ 전체 조회
    @GetMapping("/faq")
    public ResponseEntity<?> getAllFaqs() {
        try {
            List<ChatbotFaqDto> faqs = chatbotService.findAll();
            return ResponseEntity.ok(faqs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ 카테고리별 조회
    @GetMapping("/faq/category/{category}")
    public ResponseEntity<?> getFaqsByCategory(@PathVariable String category) {
        try {
            List<ChatbotFaqDto> faqs = chatbotService.findByCategory(category);
            return ResponseEntity.ok(faqs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ 질문 검색
    @GetMapping("/faq/search")
    public ResponseEntity<?> searchFaqs(@RequestParam String keyword) {
        try {
            List<ChatbotFaqDto> result = chatbotService.searchByQuestion(keyword);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ FAQ 등록
    @PostMapping("/faq")
    public ResponseEntity<?> createFaq(@RequestBody ChatbotFaqDto dto) {
        try {
            ChatbotFaqDto saved = chatbotService.save(dto);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ FAQ 단건 조회
    @GetMapping("/faq/{id}")
    public ResponseEntity<?> getFaqById(@PathVariable Long id) {
        try {
            ChatbotFaqDto faq = chatbotService.findById(id);
            return ResponseEntity.ok(faq);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ FAQ 삭제
    @DeleteMapping("/faq/{id}")
    public ResponseEntity<?> deleteFaq(@PathVariable Long id) {
        try {
            chatbotService.delete(id);
            return ResponseEntity.ok(Map.of("message", "삭제 완료"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
