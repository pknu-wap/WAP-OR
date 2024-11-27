package com.wap.wapor.repository;

import com.wap.wapor.domain.User;
import com.wap.wapor.domain.VirtualAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface VirtualAccountRepository extends JpaRepository<VirtualAccount, Long> {
    Optional<VirtualAccount> findByUser(User user);
}