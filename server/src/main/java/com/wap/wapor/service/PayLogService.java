package com.wap.wapor.service;

import com.wap.wapor.domain.*;
import com.wap.wapor.dto.PayLogResponse;
import com.wap.wapor.dto.PostPayLogDto;
import com.wap.wapor.repository.PayLogRepository;
import com.wap.wapor.repository.TransactionRepository;
import com.wap.wapor.repository.UserRepository;
import com.wap.wapor.repository.VirtualAccountRepository;
import com.wap.wapor.security.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayLogService {

    private final PayLogRepository payLogRepository;
    private final UserRepository userRepository;
    private final VirtualAccountRepository virtualAccountRepository;
    private final TransactionRepository transactionRepository; // 추가

    @Transactional
    public PayLogResponse createPayLog(PostPayLogDto postPayLogDto, UserPrincipal userPrincipal) {
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

        // 가상계좌 잔액 차감
        virtualAccount.setBalance(virtualAccount.getBalance() - postPayLogDto.getAmount());
        virtualAccountRepository.save(virtualAccount);

        // 페이로그 생성 및 저장
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

        // 거래 내역(Transaction) 생성 및 저장
        Transaction transaction = new Transaction();
        transaction.setVirtualAccount(virtualAccount);
        transaction.setTransactionType(TransactionType.WITHDRAWAL); // 출금 타입
        transaction.setAmount(postPayLogDto.getAmount());
        transaction.setUserNickname(user.getNickname()); // 사용자 닉네임 설정
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        // 잔액과 페이로그 ID를 함께 반환
        return new PayLogResponse(savedPayLog.getId(), virtualAccount.getBalance());
    }
}