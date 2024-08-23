package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByPhone(String phone);

    Optional<User> findByEmailOrUsername(String email, String username);

    Optional<User> findByEmailOrUsernameOrPhone(String email, String username, String phone);

    Optional<User> findByPhone(String phone);
}
