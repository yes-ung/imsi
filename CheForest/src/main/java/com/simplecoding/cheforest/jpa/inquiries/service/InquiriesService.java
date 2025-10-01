package com.simplecoding.cheforest.jpa.inquiries.service;


import com.simplecoding.cheforest.jpa.common.MapStruct;
import com.simplecoding.cheforest.jpa.inquiries.dto.InquiryWithNicknameDto;
import com.simplecoding.cheforest.jpa.inquiries.entity.Inquiries;
import com.simplecoding.cheforest.jpa.inquiries.repository.InquiriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiriesService {
    private final InquiriesRepository inquiriesRepository;
    private final MapStruct mapStruct;


    public void save(Inquiries inquiries) {
        inquiriesRepository.save(inquiries);
    }
   // 전체 문의 리스트 조회
    public Page<InquiryWithNicknameDto> getPagedInquiryWithNicknameDto(int page) {
        Pageable pageable = PageRequest.of(page, 10); // 0부터 시작하는 페이지
        return inquiriesRepository.findInquiryWithNickname(pageable);
    }

    public Page<InquiryWithNicknameDto> findInquiryWithNickname(Pageable pageable){
        return inquiriesRepository.findInquiryWithNickname(pageable);
    }
    // 전체 문의사항 데이터 수 카운트
    public Long countAllInquiries(){return inquiriesRepository.countAllInquiries();}

    // ANSWER_STATUS = '대기중' 인 데이터 수 카운트
    public Long countPendingInquiries(){
        return inquiriesRepository.countPendingInquiries();
    }
    // ANSWER_STATUS = '답변완료' 인 데이터 수 카운트
    public Long countAnsweredInquiries(){return inquiriesRepository.countAnsweredInquiries();}
    //  오늘 작성된 문의사항 수 카운트
    public Long countTodayInquiries(){return inquiriesRepository.countTodayInquiries();}
    // '대기중' 상태인 문의 전체 리스트 조회
    public List<InquiryWithNicknameDto> findPendingInquiriesWithNickname() {
        return inquiriesRepository.findPendingInquiriesWithNickname();
    }
    //   전체문의 사항중 검색어와 상태검색이 일치하는 것들만 조회
    public Page<InquiryWithNicknameDto> searchInquiries(String keyword, String status, Pageable pageable) {
        return inquiriesRepository.findByKeywordAndStatus(keyword, status, pageable);
    }






}
