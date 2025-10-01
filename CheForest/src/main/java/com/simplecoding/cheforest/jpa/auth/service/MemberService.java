package com.simplecoding.cheforest.jpa.auth.service;

import com.simplecoding.cheforest.jpa.auth.dto.MemberAdminDto;
import com.simplecoding.cheforest.jpa.auth.dto.MemberSignupDto;
import com.simplecoding.cheforest.jpa.auth.dto.MemberDetailDto;
import com.simplecoding.cheforest.jpa.auth.dto.MemberUpdateDto;
import com.simplecoding.cheforest.jpa.auth.entity.Member;
import com.simplecoding.cheforest.jpa.auth.repository.MemberRepository;
import com.simplecoding.cheforest.jpa.common.MapStruct;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MapStruct mapStruct;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // ================= 회원가입 =================
    public void register(MemberSignupDto dto, String serverAuthCode) {
        // 1. 중복검사
        if (memberRepository.existsByLoginId(dto.getLoginId())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        if (memberRepository.existsByNickname(dto.getNickname())) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        // 2. 이메일 인증 확인
        if (serverAuthCode == null || !serverAuthCode.equals(dto.getEmailAuthCode())) {
            throw new IllegalArgumentException("이메일 인증번호가 일치하지 않습니다.");
        }

        // 3. 비밀번호 암호화
        String encodedPw = passwordEncoder.encode(dto.getPassword());

        // 4. 엔티티 변환 및 저장
        Member member = mapStruct.toEntity(dto);
        member.setPassword(encodedPw);
        member.setRole(Member.Role.USER); // 기본 권한 USER
        member.setTempPasswordYn("N");
        member.setPoint(0L);
        member.setGrade("씨앗");

        memberRepository.save(member);
    }

    // ================= 회원정보 수정 =================
    public void update(MemberUpdateDto dto) {
        Member member = memberRepository.findById(dto.getMemberIdx())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 닉네임 중복검사 (자기자신 제외)
        if (memberRepository.existsByNicknameAndMemberIdxNot(dto.getNickname(), dto.getMemberIdx())) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        mapStruct.updateEntity(dto, member);

        memberRepository.save(member);
    }

    // ================= 회원 상세 조회 =================
    @Transactional(readOnly = true)
    public MemberDetailDto findById(Long memberIdx) {
        Member member = memberRepository.findById(memberIdx)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return mapStruct.toDetailDto(member);
    }

    // ================= 중복검사 =================
    @Transactional(readOnly = true)
    public boolean existsByLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Transactional(readOnly = true)
    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public String findLoginIdByEmail(String email) {
        return memberRepository.findIdByEmail(email);
    }

    // ADMIN 통계용 작성한 게시글,댓글수 추가한 전체 회원정보(페이지네이션)
    public Page<MemberAdminDto> adminAllMember(String keyword,Pageable pageable) {
        return memberRepository.findAllWithBoardCounts(keyword, pageable);
    }
    // ADMIN 통계용 작성한 게시글,댓글수 추가한 제재당한 회원정보(페이지네이션)
    public Page<MemberAdminDto> adminSuspendedMember(String keyword,Pageable pageable) {
        return memberRepository.findSuspendedWithBoardCounts(keyword, pageable);
    }


    //    회원 탈퇴
    @Transactional
    public void withdraw(Long memberIdx) {
        Member member = memberRepository.findById(memberIdx)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        // ✅ 회원정보 마스킹 처리
        member.setLoginId("deleted_" + member.getMemberIdx());
        member.setPassword("deleted");
        member.setNickname("탈퇴한 회원_" + member.getMemberIdx());
        member.setEmail("deleted");

        // ✅ Role을 LEFT로 변경
        member.setRole(Member.Role.LEFT);
        memberRepository.save(member);
    }

    // ================= 비밀번호 찾기: 인증번호 발송 =================
    public void sendPasswordResetCode(String loginId, String email, HttpSession session) {
        // 1. 회원 존재 여부 확인
        Member member = memberRepository.findByLoginIdAndEmail(loginId, email)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 이메일이 일치하지 않습니다."));

        // 2. 인증번호 6자리 생성
        String code = emailService.sendAuthCode(email);
        long expireAt = System.currentTimeMillis() + (5 * 60 * 1000); // 5분

        // 3. 세션에 저장
        session.setAttribute("pwResetCode", code);
        session.setAttribute("pwResetExpireAt", expireAt);
        session.setAttribute("pwResetMemberId", member.getMemberIdx());
    }

    // ================= 비밀번호 찾기: 인증번호 검증 =================
    public void verifyPasswordResetCode(String inputCode, HttpSession session) {
        String savedCode = (String) session.getAttribute("pwResetCode");
        Long expireAt = (Long) session.getAttribute("pwResetExpireAt");

        if (savedCode == null || expireAt == null) {
            throw new IllegalArgumentException("인증번호를 먼저 발급해주세요.");
        }
        if (System.currentTimeMillis() > expireAt) {
            throw new IllegalArgumentException("인증번호가 만료되었습니다.");
        }
        if (!inputCode.equals(savedCode)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        // 성공 → 세션 값 제거
        session.removeAttribute("pwResetCode");
        session.removeAttribute("pwResetExpireAt");
        // pwResetMemberId 는 이후 비밀번호 재설정 단계에서 사용 가능
    }

    // ================= 비밀번호 찾기: 비밀번호 재설정 =================
    public void resetPassword(String newPassword, HttpSession session) {
        Long memberIdx = (Long) session.getAttribute("pwResetMemberId");
        if (memberIdx == null) {
            throw new IllegalArgumentException("비밀번호 재설정 권한이 없습니다.");
        }

        Member member = memberRepository.findById(memberIdx)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        member.setPassword(passwordEncoder.encode(newPassword));

        // 비밀번호 재설정 완료 후 세션에서 제거
        session.removeAttribute("pwResetMemberId");
    }
    //  회원 제재하기(admin 용)
    public void applySuspension(Long memberIdx) {
        Member member = memberRepository.findById(memberIdx)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));
        member.setSuspension("정지");
        memberRepository.save(member);
    }













}
