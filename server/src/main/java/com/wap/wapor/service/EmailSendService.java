package com.wap.wapor.service;

import com.wap.wapor.exception.*;
import com.wap.wapor.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSendService {

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private final StringRedisTemplate stringRedisTemplate; // StringRedisTemplate 추가

    @Value("${spring.mail.username}")
    private String setFrom;

    @Autowired
    public EmailSendService(JavaMailSender mailSender, RedisUtil redisUtil, StringRedisTemplate stringRedisTemplate) {
        this.mailSender = mailSender;
        this.redisUtil = redisUtil;
        this.stringRedisTemplate = stringRedisTemplate; // 생성자 주입
    }

    // 인증 번호 생성 및 Redis에 저장 메서드
    public String requestAuthCode(String identifier) {
        String authCode = generateAuthCode();  // 인증 번호 생성
        String title = "[WAPOR] 회원 가입 인증 번호 요청 메일입니다.";
        String content = "회원 가입 인증 번호입니다." + "<br><br>" + authCode + "<br>" + "인증 번호를 정확하게 입력해 주세요.";

        // Redis에 인증 번호 저장 (key: authCode:<identifier>, 만료 시간: 5분)
        redisUtil.setDataExpire("authCode:" + identifier, authCode);

        // 이메일 전송
        sendEmail(setFrom, identifier, title, content);
        return authCode;  // 확인용으로 반환 (실제 서비스에서는 보통 반환하지 않음)
    }

    // 이메일 전송 메서드
    private void sendEmail(String setFrom, String toMail, String title, String content) {
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

    // 인증 번호 생성 메서드
    private String generateAuthCode() {
        return String.format("%06d", (int) (Math.random() * 900000) + 100000); // 6자리 인증 번호
    }

    // 인증 번호 검증 메서드
    public boolean verifyAuthCode(String identifier, String inputAuthCode) {
        String key = "authCode:" + identifier;

        // 1. 키의 TTL을 확인하여 존재 여부와 만료 여부를 확인
        Long ttl = stringRedisTemplate.getExpire(key);

        if (ttl == null || ttl == -2) {
            // 키가 아예 존재하지 않는 경우 - 이메일이 존재하지 않음
            throw new EmailNotFoundException("이메일이 존재하지 않습니다.");
        } else if (ttl == 0) {
            // 키는 존재하지만 TTL이 설정되어 있지 않은 경우 - 만료된 경우로 간주
            throw new AuthCodeExpiredException("인증 번호가 만료되었습니다.");
        }

        // 2. Redis에서 인증 코드 조회
        String storedAuthCode = redisUtil.getData(key);

        if (!storedAuthCode.equals(inputAuthCode)) {
            // 입력된 인증 번호와 저장된 인증 번호가 일치하지 않는 경우 예외 처리
            throw new AuthCodeMismatchException("인증 번호가 일치하지 않습니다.");
        }

        // 인증 성공 시 Redis에서 키 삭제
        redisUtil.deleteData(key);
        return true;
    }
}