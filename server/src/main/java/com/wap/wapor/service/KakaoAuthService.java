package com.wap.wapor.service;

import com.wap.wapor.dto.KakaoUserResponse;
import com.wap.wapor.entity.User;
import com.wap.wapor.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class KakaoAuthService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    public KakaoAuthService(RestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    public User processKakaoLogin(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                userInfoUrl, HttpMethod.GET, request, KakaoUserResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            KakaoUserResponse kakaoUserResponse = response.getBody();
            assert kakaoUserResponse != null;
            String identifier = String.valueOf(kakaoUserResponse.getId());

            Optional<User> existingUser = userRepository.findByIdentifierAndUserType(identifier, UserType.KAKAO);
            if (existingUser.isPresent()) {
                return existingUser.get(); // 기존 사용자 반환
            } else {
                // 새 사용자 등록
                User newUser = new User();
                newUser.setUserType(UserType.KAKAO);
                newUser.setIdentifier(identifier);
                newUser.setNickname(kakaoUserResponse.getKakao_account().getProfile().getNickname());
                userRepository.save(newUser);
                return newUser;
            }
        } else {
            throw new RuntimeException("Failed to retrieve Kakao user info");
        }
    }
}