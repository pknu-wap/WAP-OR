package com.wap.wapor.repository;

import com.wap.wapor.domain.User;
import com.wap.wapor.domain.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    // identifier로 사용자 조회
    Optional<User> findByIdentifier(String identifier);

    // identifier와 userType으로 사용자 조회
    Optional<User> findByIdentifierAndUserType(String identifier, UserType userType);

    // identifier 중복 여부 확인
    boolean existsByIdentifier(String identifier);

    // identifier와 여러 userType으로 사용자 조회
    Optional<User> findByIdentifierAndUserTypeIn(String identifier, List<UserType> userTypes);
}