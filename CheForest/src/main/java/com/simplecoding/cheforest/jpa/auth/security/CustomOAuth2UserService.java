package com.simplecoding.cheforest.jpa.auth.security;

import com.simplecoding.cheforest.jpa.auth.entity.Member;
import com.simplecoding.cheforest.jpa.auth.repository.MemberRepository;
import com.simplecoding.cheforest.jpa.auth.security.oauth.OAuth2UserInfo;
import com.simplecoding.cheforest.jpa.auth.security.oauth.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 서비스로 attributes 불러오기
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // 어떤 provider 인지 확인 (google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Provider별 파싱
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);

        // ✅ DB 조회 or 신규 저장
        Member member = memberRepository.findBySocialIdAndProvider(userInfo.getId(), registrationId.toUpperCase())
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .socialId(userInfo.getId())
                                .provider(registrationId.toUpperCase())
                                .email(userInfo.getEmail())
                                .nickname(userInfo.getName())
                                .profile(userInfo.getImageUrl())
                                .role(Member.Role.USER)
                                .build()
                ));

        // attributes 안에 provider 추가
        attributes.put("provider", registrationId.toUpperCase());

        // 로그인 객체 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())),
                userInfo.getAttributes(),
                "id" // 기본 키 → provider별로 다르지만, attributes는 Factory에서 정리되어 있음
        );
    }
}
