package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.SMS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SMSRepository extends JpaRepository<SMS, UUID> {
    public Optional<SMS> findBySid(String sid);


}
