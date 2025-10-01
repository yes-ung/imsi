package com.simplecoding.cheforest.jpa.chatbot.service;

import com.simplecoding.cheforest.jpa.chatbot.dto.ChatbotFaqDto;
import com.simplecoding.cheforest.jpa.chatbot.entity.ChatbotFaq;
import com.simplecoding.cheforest.jpa.chatbot.repository.ChatbotFaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final ChatbotFaqRepository faqRepository;

    // 1) FAQ 전체 조회
    public List<ChatbotFaqDto> findAll() {
        return faqRepository.findAll().stream()
                .map(faq -> new ChatbotFaqDto(
                        faq.getId(),
                        faq.getQuestion(),
                        faq.getAnswer(),
                        faq.getCategory()
                ))
                .toList();
    }

    // 2) 카테고리별 조회
    public List<ChatbotFaqDto> findByCategory(String category) {
        return faqRepository.findAll().stream()
                .filter(faq -> faq.getCategory() != null &&
                        faq.getCategory().equalsIgnoreCase(category))
                .map(faq -> new ChatbotFaqDto(
                        faq.getId(),
                        faq.getQuestion(),
                        faq.getAnswer(),
                        faq.getCategory()
                ))
                .toList();
    }

    // 3) 키워드 검색
    public List<ChatbotFaqDto> searchByQuestion(String keyword) {
        return faqRepository.findAll().stream()
                .filter(faq -> faq.getQuestion() != null &&
                        faq.getQuestion().contains(keyword))
                .map(faq -> new ChatbotFaqDto(
                        faq.getId(),
                        faq.getQuestion(),
                        faq.getAnswer(),
                        faq.getCategory()
                ))
                .toList();
    }

    // 4) FAQ 저장
    public ChatbotFaqDto save(ChatbotFaqDto dto) {
        ChatbotFaq entity = faqRepository.save(
                ChatbotFaq.builder()
                        .question(dto.getQuestion())
                        .answer(dto.getAnswer())
                        .category(dto.getCategory())
                        .build()
        );
        return new ChatbotFaqDto(
                entity.getId(),
                entity.getQuestion(),
                entity.getAnswer(),
                entity.getCategory()
        );
    }

    // 5) 단건 조회
    public ChatbotFaqDto findById(Long id) {
        ChatbotFaq faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found: " + id));
        return new ChatbotFaqDto(
                faq.getId(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getCategory()
        );
    }

    // 6) 삭제
    public void delete(Long id) {
        if (!faqRepository.existsById(id)) {
            throw new RuntimeException("삭제할 FAQ가 존재하지 않습니다: " + id);
        }
        faqRepository.deleteById(id);
    }

    // ---------------------------
    // 🤖 챗봇 로직 (DB 검색만 유지)
    // ---------------------------
    public String findAnswerFromFaq(String question) {
        return faqRepository.findTopByQuestionContainingIgnoreCase(question)
                .map(ChatbotFaq::getAnswer)
                .orElse(null);
    }
}
