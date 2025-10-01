package com.simplecoding.cheforest.jpa.auth.repository;

import com.simplecoding.cheforest.jpa.auth.dto.MemberAdminDto;
import com.simplecoding.cheforest.jpa.auth.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 로그인/중복검사
    Optional<Member> findByLoginId(String id);
    boolean existsByLoginId(String id);

    // 이메일
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);

    // 닉네임
    Optional<Member> findByNickname(String nickname);
    boolean existsByNickname(String nickname);

    // 회원정보 수정 시, 자기 자신 제외하고 닉네임 중복 확인
    boolean existsByNicknameAndMemberIdxNot(String nickname, Long memberIdx);

    // 회원번호로 조회
    Optional<Member> findByMemberIdx(Long memberIdx);

    // 소셜 로그인
    Optional<Member> findBySocialIdAndProvider(String socialId, String provider);

    // 사용자 확인 (비밀번호 찾기용)
    Optional<Member> findByLoginIdAndEmail(String id, String email);

    // JPQL: 비밀번호만 조회
    @Query("select m.password from Member m where m.memberIdx = :memberIdx")
    String findPasswordByMemberIdx(Long memberIdx);

    // JPQL: 임시비밀번호 여부 조회
    @Query("select m.tempPasswordYn from Member m where m.memberIdx = :memberIdx")
    String findTempPasswordYnByMemberIdx(Long memberIdx);

    // JPQL: 아이디 찾기 (이메일로 아이디 반환)
    @Query("select m.loginId from Member m where m.email = :email")
    String findIdByEmail(String email);

    // 포인트 상위 10명 (랭킹)
    List<Member> findTop10ByOrderByPointDesc();

    // 특정 회원의 랭킹 (내 순위 구하기)
    @Query("SELECT COUNT(m) + 1 FROM Member m WHERE m.point > :point")
    Long findMyRank(@Param("point") Long point);

    // made by yes_ung
    // ADMIN 통계용 작성한 게시글,댓글수 추가한 전체 회원정보(페이지네이션)
    @Query(
            value = """
        SELECT 
            M.MEMBER_IDX AS memberIdx,
            M.EMAIL AS email,
            M.ROLE AS role,
            M.NICKNAME AS nickname,
            M.INSERT_TIME AS insertTime,
            M.PROFILE AS profile,
            M.SOCIAL_ID AS socialId,
            M.PROVIDER AS provider,
            M.UPDATE_TIME AS updateTime,
            M.POINT AS point,
            M.GRADE AS grade,
            M.SUSPENSION AS status,        
            M.LAST_LOGIN_TIME AS lastLoginTime,
            (SELECT COUNT(*) FROM BOARD B WHERE B.WRITER_IDX = M.MEMBER_IDX) AS boardCount,
            (SELECT COUNT(*) FROM BOARD_REVIEW BR WHERE BR.WRITER_IDX = M.MEMBER_IDX) AS boardReviewCount
        FROM MEMBER M
        WHERE M.ROLE != 'LEFT'
        AND (
            M.NICKNAME LIKE %:keyword%
            OR TO_CHAR(M.MEMBER_IDX) LIKE %:keyword%
        )
        ORDER BY M.INSERT_TIME DESC
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM MEMBER M
        WHERE M.ROLE != 'LEFT'
        AND (
            M.NICKNAME LIKE %:keyword%
            OR TO_CHAR(M.MEMBER_IDX) LIKE %:keyword%
        )
        """,
            nativeQuery = true)
    Page<MemberAdminDto> findAllWithBoardCounts(@Param("keyword") String keyword,
                                                Pageable pageable);
    // ADMIN 통계용 작성한 게시글,댓글수 추가한 제재당한 회원정보(페이지네이션)
    @Query(
            value = """
        SELECT 
            M.MEMBER_IDX AS memberIdx,
            M.EMAIL AS email,
            M.ROLE AS role,
            M.NICKNAME AS nickname,
            M.INSERT_TIME AS insertTime,
            M.PROFILE AS profile,
            M.SOCIAL_ID AS socialId,
            M.PROVIDER AS provider,
            M.UPDATE_TIME AS updateTime,
            M.POINT AS point,
            M.GRADE AS grade,
            M.SUSPENSION AS status,        
            M.LAST_LOGIN_TIME AS lastLoginTime,
            (SELECT COUNT(*) FROM BOARD B WHERE B.WRITER_IDX = M.MEMBER_IDX) AS boardCount,
            (SELECT COUNT(*) FROM BOARD_REVIEW BR WHERE BR.WRITER_IDX = M.MEMBER_IDX) AS boardReviewCount
        FROM MEMBER M
        WHERE M.SUSPENSION IS NOT NULL
        AND M.ROLE != 'LEFT'
        AND (
            M.NICKNAME LIKE %:keyword%
            OR TO_CHAR(M.MEMBER_IDX) LIKE %:keyword%
        )
        """,
            countQuery =  """
        SELECT COUNT(*)
        FROM MEMBER M
        WHERE M.SUSPENSION IS NOT NULL
        AND M.ROLE != 'LEFT'
        AND (
            M.NICKNAME LIKE %:keyword%
            OR TO_CHAR(M.MEMBER_IDX) LIKE %:keyword%
        )
        """,
            nativeQuery = true)
    Page<MemberAdminDto> findSuspendedWithBoardCounts(@Param("keyword") String keyword,
                                                      Pageable pageable);


}
