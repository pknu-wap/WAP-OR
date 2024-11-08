package com.wap.wapor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSendService {
    @Autowired
    private JavaMailSender mailSender;

    private int number;

    @Value("${spring.mail.username}")
    private String setFrom;

    public void createNumber() {
        this.number = (int)(Math.random() * 900000) + 100000;  // 6자리 인증 번호 생성
    }

    public String joinEmail(String email) {
        createNumber();
        String toMail = email;
        String title = "[WAPOR] 회원 가입 인증 번호 요청 메일입니다.";
        String content =
                "회원 가입 인증 번호입니다." + "<br><br>" + number + "<br>" + "인증 번호를 정확하게 입력해 주세요.";

        mailSend(setFrom, toMail, title, content);
        return Integer.toString(number);
    }

    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MailSendException e) {
            System.err.println("이메일 전송 실패: 잘못된 이메일 주소입니다.\n에러 메시지: " + e.getMessage());
        } catch (MessagingException e) {
            System.err.println("이메일 전송 실패: " + e.getMessage());
        }
    }
}
