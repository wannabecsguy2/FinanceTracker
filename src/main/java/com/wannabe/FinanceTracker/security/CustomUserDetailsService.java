package com.wannabe.FinanceTracker.security;

import com.wannabe.FinanceTracker.exception.ResourceNotFoundException;
import com.wannabe.FinanceTracker.model.User;
import com.wannabe.FinanceTracker.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserPrincipal loadUserByUsername(String userIdentifier) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrUsername(userIdentifier, userIdentifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email/username : " + userIdentifier));

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserPrincipal loadUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return UserPrincipal.create(user);
    }
}