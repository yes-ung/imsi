package com.simplecoding.cheforest.jpa.admin.service;


import com.simplecoding.cheforest.jpa.admin.dto.AccountStatusDTO;
import com.simplecoding.cheforest.jpa.admin.dto.CountTodayNewBoardDTO;
import com.simplecoding.cheforest.jpa.admin.dto.MonthlyBoardCountDTO;
import com.simplecoding.cheforest.jpa.admin.dto.TodaySignUpUsersDto;
import com.simplecoding.cheforest.jpa.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
// made by yes_ung
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

//   총 가입자 수
    public long getMemberCount() {
        return adminRepository.getMemberCount();
    }
//    오늘 가입자수
    public long getTodayMemberCount() {
        return adminRepository.getTodayMemberCount();
    }
//    오늘 가입자 정보
    public List<TodaySignUpUsersDto> getTodayMemberList() {
        return adminRepository.getTodayMemberList();
    }
//    활동,휴먼,제재 계정수 집계
    public AccountStatusDTO getAccountStatusCounts() { return adminRepository.getAccountStatusCounts(); }
//   오늘 작성된 게시물,댓글 수
    public CountTodayNewBoardDTO getCountTodayNewBoard() { return adminRepository.getCountTodayNewBoard(); }
//    월별 작성된 게시물수 조회
    public List<MonthlyBoardCountDTO> getMonthlyBoardCount() { return adminRepository.getMonthlyBoardCount(); }
//    월별 작성된 게시물 및 가입자수 json자료형 통계용
    public String getMonthlyActivityStats() { return adminRepository.getMonthlyActivityStats(); }


}
