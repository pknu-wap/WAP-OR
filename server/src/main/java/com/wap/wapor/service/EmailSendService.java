package com.wap.wapor.service;

import com.wap.wapor.exception.AuthCodeMismatchException;
import com.wap.wapor.exception.CustomMailSendException;
import com.wap.wapor.exception.EmailValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSendService {

    private final JavaMailSender mailSender;
    private String authCode;  // 임시로 인증 번호를 저장할 변수

    @Value("${spring.mail.username}")
    private String setFrom;

    public EmailSendService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 인증 번호 생성 메서드
    public void createAuthCode() {
        this.authCode = String.format("%06d", (int)(Math.random() * 900000) + 100000);  // 6자리 인증 번호 생성
    }

    // 인증 번호 생성 후 이메일 전송
    public String joinEmail(String email) {
        createAuthCode(); // 인증 번호 생성
        String title = "[WAPOR] 회원 가입 인증 번호 요청 메일입니다.";
        String content = "회원 가입 인증 번호입니다." + "<br><br>" + authCode + "<br>" + "인증 번호를 정확하게 입력해 주세요.";

        mailSend(setFrom, email, title, content);  // 이메일 전송
        return authCode;    // 생성된 인증 번호 반환 (확인용)
    }

    // 이메일 전송 메서드
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
            throw new EmailValidationException("잘못된 이메일 주소입니다.");
        } catch (MessagingException e) {
            throw new CustomMailSendException("이메일을 입력해 주세요.");
        }
    }

    // 인증 번호 확인 메서드
    public boolean verifyAuthCode(String inputAuthCode) {
        if (authCode == null || !authCode.equals(inputAuthCode)) {
            throw new AuthCodeMismatchException("인증 번호를 다시 확인해 주세요.");
        }
        return true;
    }
}