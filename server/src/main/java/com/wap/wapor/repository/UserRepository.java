package com.wap.wapor.repository;

import com.wap.wapor.domain.User;
import com.wap.wapor.domain.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByIdentifier(String identifier);
    Optional<User> findByIdentifierAndUserType(String identifier, UserType usertype);
    // identifier 중복 여부 확인 메서드
    boolean existsByIdentifier(String identifier);
}
