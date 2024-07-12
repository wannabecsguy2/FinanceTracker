package com.wannabe.FinanceTracker.repository;

import com.wannabe.FinanceTracker.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

}
