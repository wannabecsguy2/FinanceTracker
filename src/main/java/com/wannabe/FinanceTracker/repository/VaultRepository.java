package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.Vault;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VaultRepository extends JpaRepository<Vault, UUID> {
}
