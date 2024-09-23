package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.OTP;
import com.wannabe.FinanceTracker.model.OTPType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OTPRepository extends JpaRepository<OTP, Long> {
    Optional<List<OTP>> findAllByUserIdAndOtpAndVerifiedAndTypeAndExpiresAfter(UUID userId, String otp, boolean verified, OTPType type, LocalDateTime expires);
}
