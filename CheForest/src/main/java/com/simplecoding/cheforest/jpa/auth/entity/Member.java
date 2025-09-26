package com.simplecoding.cheforest.jpa.auth.entity;

import com.simplecoding.cheforest.jpa.chat.entity.Message;
import com.simplecoding.cheforest.jpa.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Entity
@Table(name = "MEMBER")
@SequenceGenerator(
        name = "MEMBER_SEQ_JPA",
        sequenceName = "MEMBER_SEQ",
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_JPA")
    private Long memberIdx;   // PK (DB 컬럼: MEMBERIDX 로 맞춰야 함)

    @Column(name = "ID", nullable = false, unique = true)
    private String loginId;  // 로그인 ID
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;        // USER / ADMIN
    private String nickname;
    private String profile;
    private String tempPasswordYn = "N";  // 기본값 N
    private String socialId;   // 카카오, 구글, 네이버 식별자
    private String provider;   // "KAKAO", "GOOGLE", "NAVER"
    private Long point = 0L;      // 누적 포인트
    private String grade = "씨앗"; // 회원 등급

    // made by yes_ung 09/25
    private Date lastLoginTime;

    public enum Role {
        USER, ADMIN
    }

    // 채팅용
    @OneToMany(mappedBy = "sender")
    private List<Message> messages = new ArrayList<>();
}
