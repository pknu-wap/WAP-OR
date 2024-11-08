package com.wap.wapor.service;
import com.wap.wapor.dto.EmailLoginRequest;
import com.wap.wapor.domain.User;
import com.wap.wapor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired // 스프링이 UserRepository의 구현체를 자동으로 주입
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> emailLogin(EmailLoginRequest emailLoginRequest) {
        // identifier 값으로 사용자를 검색
        // 검색된 사용자가 없으면 user 변수에 null 할당
        User user = userRepository.findByIdentifier(emailLoginRequest.getIdentifier()).orElse(null);

        // 사용자가 존재하고 비밀번호가 입력된 비밀번호와 일치하는지
        if (user != null && user.getPassword().equals(emailLoginRequest.getPassword())) {
            // 로그인 성공 처리
            return ResponseEntity.ok("Email login successful");
        } else {
            // 인증 실패 처리
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}
