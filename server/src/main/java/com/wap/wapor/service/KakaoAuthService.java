package com.wap.wapor.service;

import com.wap.wapor.domain.VirtualAccount;
import com.wap.wapor.dto.AuthResponse;
import com.wap.wapor.dto.KakaoUserResponse;
import com.wap.wapor.domain.User;
import com.wap.wapor.domain.UserType;
import com.wap.wapor.repository.UserRepository;
import com.wap.wapor.repository.VirtualAccountRepository;
import com.wap.wapor.security.JwtTokenProvider;
import com.wap.wapor.security.UserPrincipal;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class KakaoAuthService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final VirtualAccountRepository virtualAccountRepository;
    private static final Logger logger = LoggerFactory.getLogger(KakaoAuthService.class);


    public KakaoAuthService(RestTemplate restTemplate, UserRepository userRepository, JwtTokenProvider jwtTokenProvider,VirtualAccountRepository virtualAccountRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.virtualAccountRepository = virtualAccountRepository;
    }

    public AuthResponse processKakaoLogin(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                userInfoUrl, HttpMethod.GET, request, KakaoUserResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String token;
            User user;
            KakaoUserResponse kakaoUserResponse = response.getBody();
            assert kakaoUserResponse != null;
            String identifier = String.valueOf(kakaoUserResponse.getId());
            UserPrincipal userPrincipal = new UserPrincipal(identifier,kakaoUserResponse.getKakaoAccount().getEmail().split("@")[0]);
            Optional<User> existingUser = userRepository.findByIdentifierAndUserType(identifier, UserType.KAKAO);
            token=jwtTokenProvider.generateToken(userPrincipal);
            if (existingUser.isPresent()) {
                user= existingUser.get(); // 기존 사용자 반환
            } else {
                // 새 사용자 등록
                User newUser = new User();
                newUser.setUserType(UserType.KAKAO);
                newUser.setIdentifier(identifier);
                newUser.setNickname(kakaoUserResponse.getKakaoAccount().getEmail().split("@")[0]); //@앞까지만 추출해서 닉네임으로 사용
                user=userRepository.save(newUser);
                VirtualAccount virtualAccount = new VirtualAccount();  //새로운 가상계좌 생성
                virtualAccount.setUser(user); // 사용자와 계좌 연결
                virtualAccount.setBalance(0L); // 초기 잔액 설정
                virtualAccountRepository.save(virtualAccount);
            }
           return new AuthResponse(token,user);
         } else {
            throw new RuntimeException("Failed to retrieve Kakao user info");
        }
    }
}