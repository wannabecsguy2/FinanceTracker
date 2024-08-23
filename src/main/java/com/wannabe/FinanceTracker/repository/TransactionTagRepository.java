package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.TransactionTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionTagRepository extends JpaRepository<TransactionTag, Long> {
}
