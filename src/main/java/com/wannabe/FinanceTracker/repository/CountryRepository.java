package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    public boolean existsByExtension(String extension);
}
