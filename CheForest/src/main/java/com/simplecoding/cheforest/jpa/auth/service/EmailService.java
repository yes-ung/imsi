package com.simplecoding.cheforest.jpa.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // 인증번호 생성
    private String createCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리 숫자
        return String.valueOf(code);
    }

    // 인증 메일 발송
    public String sendAuthCode(String to) {
        String code = createCode();
        String subject = "[CheForest] 이메일 인증번호 안내";
        String content = "<h3>인증번호는 <b>" + code + "</b> 입니다.</h3>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송 실패", e);
        }

        return code; // 컨트롤러에서 세션에 저장
    }
}
