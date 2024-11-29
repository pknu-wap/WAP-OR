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
        String title = "[WAPOR] 이메일 인증번호입니다.";
        String content =
                "<div style='font-family: Arial, sans-serif; max-width: 900px; margin: 0 auto; border-radius: 10px; overflow: hidden;'>" +
                        // 상단 헤더 및 로고
                        "<div style='background: linear-gradient(90deg, #3183FF 0%, #79AFFF 100%); padding: 40px; text-align: center;'>" +
                        "<img src='https://storage.googleapis.com/wapor_email/icon.png' alt='앱 로고' style='height: 60px; width: auto; max-width: 100%; margin-bottom: 20px;'>" +
                        "</div>" +
                        // 본문 내용
                        "<div style='padding: 30px; background-color: #ffffff;'>" +
                        "<p style='font-size: 20px; color: #333; text-align: center; font-weight: bold;'>와포어 가입을 환영합니다!</p><br>" +
                        "<p style='font-size: 16px; color: #555; text-align: center; line-height: 1.8;'>" +
                        "안녕하세요, <img src='https://storage.googleapis.com/wapor_email/logo.png' alt='와포어 로고' style='height: 24px; width: auto; vertical-align: middle; margin-left: 5px; margin-right: 5px;'>입니다.<br>" +
                        "와포어는 오픈채팅 거지방을 모티브로 한 소비 기록을 공유하는 커뮤니티 기반 가계부 앱입니다.<br>" +
                        "지금 인증 절차를 완료하고 와포어의 다양한 기능을 경험해 보세요." +
                        "</p>" +
                        // 인증번호
                        "<div style='text-align: center; margin: 30px 0;'>" +
                        "<span style='font-size: 32px; font-weight: bold; color: #3183FF;'>" +
                        authCode +
                        "</span>" +
                        "</div>" +
                        "<p style='font-size: 14px; color: #888; text-align: center;'>" +
                        "위 인증번호를 정확히 입력한 후 이메일 인증을 완료하세요.<br>" +
                        "제한 시간 5분이 지나면 이메일 인증을 다시 요청해야 합니다." +
                        "</p>" +
                        // 주요 기능 소개
                        "<div style='margin: 20px 0; padding: 15px; background-color: #f1f7ff; border-radius: 10px;'>" +
                        "<p style='font-size: 14px; color: #555; text-align: left;'>✔ 와포어의 주요 기능:</p>" +
                        "<ul style='font-size: 14px; color: #555; text-align: left; padding-left: 20px;'>" +
                        "<li>지출이 있을 때마다 페이 로그를 작성하여 소비 기록을 공유해요.</li>" +
                        "<li>다양한 사람들과 자유롭게 소통할 수 있어요.</li>" +
                        "<li>가상 계좌를 통해 소비 목표 금액을 설정하고 입출금 내역을 확인할 수 있어요.</li>" +
                        "<li>분석 그래프를 통해 소비 습관을 개선하기는 곧... 완성돼요.</li>" +
                        "</ul>" +
                        "</div>" +
                        // 깃허브 링크
                        "<p style='font-size: 14px; color: #555; text-align: center;'>와포어의 개발 과정이 궁금하다면?</p>" +
                        "<div style='text-align: center; margin-top: 20px;'>" +
                        "<a href='https://github.com/pknu-wap/WAP-OR' target='_blank' style='text-decoration: none; color: #3183FF; font-size: 16px; font-weight: bold;'>🔗 깃허브 탐색하기</a>" +
                        "</div>" +
                        "</div>" +
                        // 푸터
                        "<div style='background-color: #3183FF; color: #fff; padding: 40px; text-align: center; font-size: 12px;'>" +
                        "<p>본 메일은 발신 전용이며 문의에 대한 회신은 처리되지 않습니다.</p>" +
                        "<p>© 2024 WAPOR. All Rights Reserved</p>" +
                        "</div>" +
                        "</div>";

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