package com.simplecoding.cheforest.jpa.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {

    private Long senderId; // message 테이블에서 member 테이블로부터 fk로 발신자 식별 (식별용)
    private String sender; // 발신자 닉네임 (출력용)
    private String message; // message 테이블에 content

    private String profile; // 프로필 사진

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") // 날짜 포멧 설정
    private LocalDateTime time ; // message 테이블에 message date
}
