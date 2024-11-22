package com.wap.wapor.service;

import com.wap.wapor.domain.PayLog;
import com.wap.wapor.domain.User;
import com.wap.wapor.domain.UserType;
import com.wap.wapor.dto.PostPayLogDto;
import com.wap.wapor.repository.PayLogRepository;
import com.wap.wapor.repository.UserRepository;
import com.wap.wapor.security.UserPrincipal;
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

}
