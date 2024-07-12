package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.Email;
import com.wannabe.FinanceTracker.model.EmailStatus;
import com.wannabe.FinanceTracker.model.EmailType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmailRepository extends JpaRepository<Email, UUID> {
    List<Email> findByUserIdAndTypeAndStatus(UUID userId, EmailType type, EmailStatus status);
}
