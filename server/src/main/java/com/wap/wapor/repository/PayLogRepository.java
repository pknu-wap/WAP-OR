package com.wap.wapor.repository;

import com.wap.wapor.domain.PayLog;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wap.wapor.domain.PayLog;

import java.awt.print.Pageable;

public interface PayLogRepository extends JpaRepository<PayLog, Long> {
    Page<PayLog> findByIsPublicTrue(Pageable pageable);
}
