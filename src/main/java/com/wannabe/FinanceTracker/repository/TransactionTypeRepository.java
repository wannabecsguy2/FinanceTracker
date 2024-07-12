package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {
}
