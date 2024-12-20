package com.wap.wapor.service;

import com.wap.wapor.domain.User;
import com.wap.wapor.domain.UserType;
import com.wap.wapor.domain.VirtualAccount;
import com.wap.wapor.dto.AuthResponse;
import com.wap.wapor.dto.EmailLoginRequest;
import com.wap.wapor.exception.EmailNotVerifiedException;
import com.wap.wapor.exception.InvalidCredentialsException;
import com.wap.wapor.exception.InvalidPasswordException;
import com.wap.wapor.exception.UserAlreadyExistsException;
import com.wap.wapor.repository.UserRepository;
import com.wap.wapor.repository.VirtualAccountRepository;
import com.wap.wapor.security.JwtTokenProvider;
import com.wap.wapor.security.UserPrincipal;
import com.wap.wapor.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VirtualAccountRepository virtualAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository,
                       VirtualAccountRepository virtualAccountRepository,
                       PasswordEncoder passwordEncoder,
                       RedisUtil redisUtil,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.virtualAccountRepository = virtualAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisUtil = redisUtil;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 비밀번호 유효성 검사 메서드
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$";
        return Pattern.matches(passwordPattern, password);
    }

    // 회원가입 메서드
    public ResponseEntity<String> registerUser(String identifier, String password) {
        // 이메일 인증 상태 확인
        String verificationStatus = redisUtil.getData("verified:" + identifier);
        if (verificationStatus == null || !verificationStatus.equals("true")) {
            throw new EmailNotVerifiedException("이메일 인증이 완료되지 않았습니다.");
        }

        if (userRepository.existsByIdentifier(identifier)) {
            throw new UserAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 유효성 검사
        if (!isValidPassword(password)) {
            throw new InvalidPasswordException("비밀번호는 문자와 숫자를 포함하여 8~20자입니다.");
        }

        // 사용자 생성
        User user = new User();
        user.setIdentifier(identifier);
        user.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화
        user.setUserType(UserType.EMAIL); // 사용자 유형 설정
        user.setNickname(identifier.split("@")[0]); // 닉네임을 이메일의 @ 앞부분으로 설정

        User savedUser = userRepository.save(user);

        // 계좌 생성 및 사용자와 연결
        VirtualAccount virtualAccount = new VirtualAccount();
        virtualAccount.setUser(savedUser); // 사용자와 계좌 연결
        virtualAccount.setBalance(0L); // 초기 잔액 설정
        virtualAccountRepository.save(virtualAccount);

        // 회원가입 후 Redis에서 인증 상태 제거 (재사용 방지)
        redisUtil.deleteData("verified:" + identifier);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully with a virtual account");
    }

    // 이메일 로그인 메서드 (JWT 토큰 발급 포함)
    public ResponseEntity<AuthResponse> authenticateUser(EmailLoginRequest emailLoginRequest) {
        // 사용자 확인
        User user = userRepository.findByIdentifier(emailLoginRequest.getIdentifier())
                .orElseThrow(() -> new InvalidCredentialsException("잘못된 이메일 또는 비밀번호입니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(emailLoginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("잘못된 이메일 또는 비밀번호입니다.");
        }

        // JWT 토큰 생성
        UserPrincipal userPrincipal = new UserPrincipal(user.getIdentifier(), user.getNickname());
        String token = jwtTokenProvider.generateToken(userPrincipal);

        // AuthResponse 생성 및 반환
        AuthResponse authResponse = new AuthResponse(token, user);
        return ResponseEntity.ok(authResponse);
    }
}