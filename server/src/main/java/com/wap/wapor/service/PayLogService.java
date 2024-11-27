package com.wap.wapor.service;

import com.wap.wapor.domain.PayLog;
import com.wap.wapor.domain.User;
import com.wap.wapor.domain.UserType;
import com.wap.wapor.dto.GetPayLogDto;
import com.wap.wapor.dto.PostPayLogDto;
import com.wap.wapor.repository.PayLogRepository;
import com.wap.wapor.repository.UserRepository;
import com.wap.wapor.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PayLogService {
        private final PayLogRepository payLogRepository;
        private final UserRepository userRepository;
        public PayLogService(PayLogRepository payLogRepository, UserRepository userRepository) {
            this.payLogRepository = payLogRepository;
            this.userRepository = userRepository;
        }
    public Long payLogPost(PostPayLogDto postPayLogDto, UserPrincipal userPrincipal) {
        Optional<User> user=userRepository.findByIdentifierAndUserType(userPrincipal.getId(), UserType.KAKAO);
        PayLog payLog=new PayLog();
        payLog.setAmount(postPayLogDto.getAmount());
        payLog.setCategory(postPayLogDto.getCategory());
        payLog.setContent(postPayLogDto.getContent());
        payLog.setTitle(postPayLogDto.getTitle());
        payLog.setCreatedAt(LocalDateTime.now());
        payLog.setImgUrl(postPayLogDto.getImgUrl());
        payLog.setUser(user.get());
        payLog.setLikeCount(0);
        PayLog payLogResult=payLogRepository.save(payLog);
        return payLogResult.getId();
    }
    public Page<GetPayLogDto> getPublicPayLogs(Pageable pageable) {
        return payLogRepository.findByIsPublic(1,pageable)
                .map(payLog -> {
                    return new GetPayLogDto(
                            payLog.getTitle(),
                            payLog.getContent(),
                            payLog.getImgUrl(),
                            payLog.getCategory(),
                            payLog.getAmount(),
                            payLog.getLikeCount(),
                            payLog.getUser().getIdentifier(),
                            payLog.getUser().getNickname()

                    );
                });
    }

    public void deletePayLog(Long id,UserPrincipal userPrincipal) {
        PayLog payLog = payLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PayLog with id " + id + " does not exist"));

        // 현재 사용자와 PayLog 작성자가 같은지 확인
        if (!payLog.getUser().getIdentifier().equals(userPrincipal.getId())) {
            throw new SecurityException("You do not have permission to delete this PayLog");
        }

        payLogRepository.deleteById(id);
    }

}
