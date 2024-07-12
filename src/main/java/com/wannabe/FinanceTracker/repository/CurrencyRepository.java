package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    public boolean existsByCodeOrName(String code, String name);
}
