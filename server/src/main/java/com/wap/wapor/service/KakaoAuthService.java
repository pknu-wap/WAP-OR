package com.wap.wapor.service;

import com.wap.wapor.dto.KakaoUserResponse;

public class KakaoAuthService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public KakaoAuthService(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public String processKakaoLogin(String accessToken) {
        KakaoUserResponse kakaoUser = getKakaoUserInfo(accessToken);

        if (kakaoUser == null) {
            throw new RuntimeException("카카오 사용자 정보를 가져올 수 없습니다.");
        }

        // 사용자 정보가 DB에 있는지 확인
        User user = userRepository.findBySocialIdAndProvider(kakaoUser.getId(), UserProvider.KAKAO)
                .orElseGet(() -> registerNewUser(kakaoUser)); // 신규 사용자인 경우 등록

        // JWT 토큰 생성 로직 추가 가능
        return "Login successful for user: " + user.getUsername();
    }

    private KakaoUserResponse getKakaoUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                userInfoUrl, HttpMethod.GET, request, KakaoUserResponse.class);

        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : null;
    }

    private User registerNewUser(KakaoUserResponse kakaoUser) {
        User newUser = new User();
        newUser.setSocialId(kakaoUser.getId());
        newUser.setProvider(UserProvider.KAKAO);
        newUser.setUsername(kakaoUser.getNickname());
        newUser.setProfileImageUrl(kakaoUser.getProfileImageUrl());
        newUser.setEmail(kakaoUser.getEmail());

        return userRepository.save(newUser);
    }
}
