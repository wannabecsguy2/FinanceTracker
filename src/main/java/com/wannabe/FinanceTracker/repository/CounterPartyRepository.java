package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.CounterParty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CounterPartyRepository extends JpaRepository<CounterParty, UUID> {
}
