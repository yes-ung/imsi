package com.simplecoding.cheforest.jpa.point.repository;

import com.simplecoding.cheforest.jpa.point.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    // 1. 오늘 획득 포인트 합산, 결과 없을경우 0
    @Query(value = "SELECT NVL(SUM(p.POINT), 0) " +
            "FROM POINT_HISTORY p " +
            "WHERE p.MEMBER_IDX = :memberIdx " +
            "AND TRUNC(p.CREATED_DATE) = TRUNC(SYSDATE)",
            nativeQuery = true)
    Long sumTodayPoints(@Param("memberIdx") Long memberIdx);

    // 2. 특정 기간 동안 획득 포인트 합산 (주간/월간)
    @Query("SELECT COALESCE(SUM(p.point), 0) " +
            "FROM PointHistory p " +
            "WHERE p.member.memberIdx = :memberId " +
            "AND p.insertTime BETWEEN :start AND :end")
    Long sumPointsInPeriod(@Param("memberId") Long memberId,
                           @Param("start") LocalDateTime start,
                           @Param("end") LocalDateTime end);

    // 3. 최근 포인트 이력 (N개)
    List<PointHistory> findTop5ByMember_MemberIdxOrderByInsertTimeDesc(Long memberId);
}
