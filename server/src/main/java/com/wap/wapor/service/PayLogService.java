package com.wap.wapor.service;

import com.wap.wapor.domain.PayLog;
import com.wap.wapor.domain.User;
import com.wap.wapor.domain.UserType;
import com.wap.wapor.domain.VirtualAccount;
import com.wap.wapor.dto.PostPayLogDto;
import com.wap.wapor.repository.PayLogRepository;
import com.wap.wapor.repository.UserRepository;
import com.wap.wapor.repository.VirtualAccountRepository;
import com.wap.wapor.security.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayLogService {

    private final PayLogRepository payLogRepository;
    private final UserRepository userRepository;
    private final VirtualAccountRepository virtualAccountRepository;

    @Transactional
    public Long createPayLog(PostPayLogDto postPayLogDto, UserPrincipal userPrincipal) {
        // KAKAO와 EMAIL 두 가지 유형을 허용
        List<UserType> allowedUserTypes = Arrays.asList(UserType.KAKAO, UserType.EMAIL);

        // identifier와 userType 목록으로 사용자 조회
        User user = userRepository.findByIdentifierAndUserTypeIn(userPrincipal.getId(), allowedUserTypes)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        VirtualAccount virtualAccount = virtualAccountRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("가상계좌를 찾을 수 없습니다."));

        if (postPayLogDto.getAmount() == null || postPayLogDto.getAmount() <= 0) {
            throw new IllegalArgumentException("출금 금액은 0보다 커야 합니다.");
        }

        if (virtualAccount.getBalance() < postPayLogDto.getAmount()) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        virtualAccount.setBalance(virtualAccount.getBalance() - postPayLogDto.getAmount());
        virtualAccountRepository.save(virtualAccount);

        PayLog payLog = new PayLog();
        payLog.setUser(user);
        payLog.setAmount(postPayLogDto.getAmount()); // 출금 금액
        payLog.setCategory(postPayLogDto.getCategory());
        payLog.setContent(postPayLogDto.getContent());
        payLog.setTitle(postPayLogDto.getTitle());
        payLog.setImgUrl(postPayLogDto.getImgUrl());
        payLog.setCreatedAt(LocalDateTime.now());
        payLog.setLikeCount(0);

        PayLog savedPayLog = payLogRepository.save(payLog);

        return savedPayLog.getId(); // 생성된 페이로그 ID 반환
    }
}