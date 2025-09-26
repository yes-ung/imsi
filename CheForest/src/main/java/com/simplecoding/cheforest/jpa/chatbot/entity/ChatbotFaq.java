package com.simplecoding.cheforest.jpa.chatbot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CHATBOT_FAQ")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatbotFaq {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faq_seq")
    @SequenceGenerator(
            name = "faq_seq",
            sequenceName = "SEQ_CHATBOT_FAQ", // ✅ DB 시퀀스명
            allocationSize = 1
    )
    @Column(name = "FAQ_ID")   // ✅ DB 컬럼명과 매칭
    private Long id;

    @Column(name = "QUESTION", nullable = false, length = 500)
    private String question;

    @Column(name = "ANSWER", nullable = false, length = 2000)
    private String answer;

    @Column(name = "CATEGORY", length = 100)
    private String category;
}
