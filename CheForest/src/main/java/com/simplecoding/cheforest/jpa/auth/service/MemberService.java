package com.simplecoding.cheforest.jpa.auth.service;

import com.simplecoding.cheforest.jpa.auth.dto.MemberAdminDto;
import com.simplecoding.cheforest.jpa.auth.dto.MemberSignupDto;
import com.simplecoding.cheforest.jpa.auth.dto.MemberDetailDto;
import com.simplecoding.cheforest.jpa.auth.dto.MemberUpdateDto;
import com.simplecoding.cheforest.jpa.auth.entity.Member;
import com.simplecoding.cheforest.jpa.auth.repository.MemberRepository;
import com.simplecoding.cheforest.jpa.common.MapStruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MapStruct mapStruct;
    private final PasswordEncoder passwordEncoder;

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

    // ADMIN 통계용 작성한 게시글,댓글수 추가한 전체 회원정보(페이지네이션)
    public Page<MemberAdminDto> adminAllMember(Pageable pageable) {
        return memberRepository.findAllWithBoardCounts(pageable);
    }
    // ADMIN 통계용 작성한 게시글,댓글수 추가한 제재당한 회원정보(페이지네이션)
    public Page<MemberAdminDto> adminSuspendedMember(Pageable pageable) {
        return memberRepository.findSuspendedWithBoardCounts(pageable);
    }





}
