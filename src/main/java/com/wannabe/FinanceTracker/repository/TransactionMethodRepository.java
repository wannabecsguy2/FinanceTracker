package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.TransactionMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionMethodRepository extends JpaRepository<TransactionMethod, Long> {
}
