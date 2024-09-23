package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.CounterParty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CounterPartyRepository extends JpaRepository<CounterParty, UUID> {
    Boolean existsByName(String name);

    List<CounterParty> findByUserId(UUID userId);

    Optional<CounterParty> findByUserIdAndId(UUID userId, UUID id);
}
