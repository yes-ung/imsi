package com.simplecoding.cheforest.jpa.inquiries.repository;


import com.simplecoding.cheforest.jpa.inquiries.dto.InquiryWithNicknameDto;
import com.simplecoding.cheforest.jpa.inquiries.entity.Inquiries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiriesRepository extends JpaRepository<Inquiries, Long> {

    @Query("SELECT new com.simplecoding.cheforest.jpa.inquiries.dto.InquiryWithNicknameDto(" +
            "i.inquiryId, i.memberIdx, i.title, i.questionContent, i.answerContent, " +
            "i.answerStatus, i.isFaq, i.createdAt, i.answerAt, m.nickname) " +
            "FROM Inquiries i " +
            "JOIN Member m ON i.memberIdx = m.memberIdx " )
    Page<InquiryWithNicknameDto> findInquiryWithNickname(Pageable pageable);

    // 전체 문의사항 데이터 수 카운트
    @Query("SELECT COUNT(i) FROM Inquiries i")
    long countAllInquiries();
    
    // ANSWER_STATUS = '대기중' 인 데이터 수 카운트
    @Query("SELECT COUNT(i) FROM Inquiries i WHERE i.answerStatus = '대기중'")
    long countPendingInquiries();

    // ANSWER_STATUS = '답변완료' 인 데이터 수 카운트
    @Query("SELECT COUNT(i) FROM Inquiries i WHERE i.answerStatus = '답변완료'")
    long countAnsweredInquiries();
    //  오늘 작성된 문의사항 수 카운트
    @Query(value = "SELECT COUNT(*) FROM INQUIRIES WHERE TRUNC(CREATED_AT) = TRUNC(SYSDATE)", nativeQuery = true)
    long countTodayInquiries();

    // '대기중' 상태인 문의 전체 리스트 조회
    @Query("SELECT new com.simplecoding.cheforest.jpa.inquiries.dto.InquiryWithNicknameDto(" +
            "i.inquiryId, i.memberIdx, i.title, i.questionContent, i.answerContent, " +
            "i.answerStatus, i.isFaq, i.createdAt, i.answerAt, m.nickname) " +
            "FROM Inquiries i " +
            "JOIN Member m ON i.memberIdx = m.memberIdx " +
            "WHERE i.answerStatus = '대기중'")
    List<InquiryWithNicknameDto> findPendingInquiriesWithNickname();

   //   전체문의 사항중 검색어와 상태검색이 일치하는 것들만 조회
   @Query("SELECT new com.simplecoding.cheforest.jpa.inquiries.dto.InquiryWithNicknameDto(" +
           "i.inquiryId, i.memberIdx, i.title, i.questionContent, i.answerContent, " +
           "i.answerStatus, i.isFaq, i.createdAt, i.answerAt, m.nickname) " +
           "FROM Inquiries i " +
           "JOIN Member m ON i.memberIdx = m.memberIdx " +
           "WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.questionContent) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.answerContent) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:status IS NULL OR :status = 'all' OR i.answerStatus = :status)")
   Page<InquiryWithNicknameDto> findByKeywordAndStatus(@Param("keyword") String keyword,
                                                       @Param("status") String status,
                                                       Pageable pageable);





}
