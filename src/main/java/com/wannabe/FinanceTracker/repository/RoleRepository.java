package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    List<Role> findByLevelGreaterThanEqual(Long level);

    Optional<Role> findTopByOrderByLevelDesc();
}
