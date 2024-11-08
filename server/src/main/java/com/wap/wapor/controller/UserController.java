package com.wap.wapor.controller;
import com.wap.wapor.dto.EmailLoginRequest;
import com.wap.wapor.repository.UserRepository;
import com.wap.wapor.service.UserService;
import com.wap.wapor.dto.LoginResponse;
import com.wap.wapor.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login/email")
    public ResponseEntity<LoginResponse> emailLogin(@RequestBody EmailLoginRequest emailLoginRequest) {
        // identifier 값으로 사용자를 검색
        User user = userRepository.findByIdentifier(emailLoginRequest.getIdentifier()).orElse(null);

        // 사용자가 존재하고 비밀번호가 일치하는지 확인
        if (user != null && user.getPassword().equals(emailLoginRequest.getPassword())) {
            // 로그인 성공 시 JSON 응답
            LoginResponse response = new LoginResponse("success", "Email login successful");
            return ResponseEntity.ok(response);
        } else {
            // 인증 실패 시 JSON 응답
            LoginResponse errorResponse = new LoginResponse("fail", "Invalid email or password");
            return ResponseEntity.status(401).body(errorResponse);
        }
    }
}
