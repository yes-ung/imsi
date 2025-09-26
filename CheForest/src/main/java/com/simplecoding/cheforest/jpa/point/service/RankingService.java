package com.simplecoding.cheforest.jpa.point.service;

import com.simplecoding.cheforest.jpa.auth.entity.Member;
import com.simplecoding.cheforest.jpa.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingService {

    private final MemberRepository memberRepository;

    // 상위 10명 랭킹
    public List<Member> getTopRanking(int limit) {
        return memberRepository.findTop10ByOrderByPointDesc();
    }

    // 내 순위
    public Long getMyRank(Member member) {
        return memberRepository.findMyRank(member.getPoint());
    }

    // 회원 등급 조회 (5단계 자동 계산)
    public String getMemberGrade(Long point) {
        if (point == null) return "씨앗";
        if (point < 1000) return "씨앗";
        else if (point < 2000) return "뿌리";
        else if (point < 3000) return "새싹";
        else if (point < 4000) return "나무";
        else return "숲";
    }
}
