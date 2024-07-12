package com.wannabe.FinanceTracker.service;

import com.wannabe.FinanceTracker.exception.ParameterValidationFailedException;
import com.wannabe.FinanceTracker.exception.ResourceNotFoundException;
import com.wannabe.FinanceTracker.model.Role;
import com.wannabe.FinanceTracker.model.User;
import com.wannabe.FinanceTracker.model.UserProfile;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.payload.LoginRequest;
import com.wannabe.FinanceTracker.payload.SignUpRequest;
import com.wannabe.FinanceTracker.repository.RoleRepository;
import com.wannabe.FinanceTracker.repository.UserProfileRepository;
import com.wannabe.FinanceTracker.repository.UserRepository;

import com.wannabe.FinanceTracker.utils.CommonFunctionsUtils;
import com.wannabe.FinanceTracker.utils.JWTUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private CommonFunctionsUtils commonFunctionsUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public GenericResponseObject<?> signUp(SignUpRequest signUpRequest) throws Exception {
        try {
            validateSignUpRequest(signUpRequest);
        } catch (ParameterValidationFailedException e) {
            throw new ParameterValidationFailedException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unable to register user");
        }

        User user = new User();
        UserProfile userProfile = new UserProfile();
        List<Role> userRoles = new ArrayList<>();

        Role userRole = roleRepository.findTopByOrderByLevelDesc().orElse(null);
        if (userRole == null) {
            throw new ResourceNotFoundException("Lowest Default role not found");
        }
        userRoles.add(userRole);

        user.setEmail(signUpRequest.getEmail());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRoles(userRoles);

        commonFunctionsUtils.mapUserProfile(userProfile, signUpRequest);

        try {
            userProfile = userProfileRepository.save(userProfile);
        } catch (Exception e) {
            log.error("Exception occurred while trying to save user profile", e);
            throw new RuntimeException("Unable to register user");
        }

        user.setUserProfile(userProfile);
        try {
            user = userRepository.save(user);
            return new GenericResponseObject<>(true, "User registered successfully");
        } catch (Exception e) {
            log.error("Exception occurred while trying to register new user", e);
            throw new RuntimeException("Unable to register user");
        }
    }

    public GenericResponseObject<?> login(LoginRequest loginRequest) throws Exception {
        log.info("Received login request for user with email/username: " + loginRequest.getEmail() + "/" + loginRequest.getUsername());

        String userIdentifier = loginRequest.getUsername() != null ? loginRequest.getUsername() : loginRequest.getEmail();

        if(userIdentifier == null || userIdentifier.isEmpty()) {
            log.error("User identifier is null or empty");
            throw new ParameterValidationFailedException("Username or Email ID is required");
        }

        User user = userRepository.findByEmailOrUsername(userIdentifier, userIdentifier).orElseThrow(() -> new ResourceNotFoundException("User", "email/username", userIdentifier));

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userIdentifier, loginRequest.getPassword(), user.getRoles()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.createJWT(authentication);

        if (!user.isEmailVerified()) {
            return new GenericResponseObject<>(false, "Please verify email first", jwt);
        }

        return new GenericResponseObject<>(true, "User logged in successfully", jwt);
    }

    private void validateSignUpRequest(SignUpRequest signUpRequest) throws ParameterValidationFailedException {
        // Password Validation
        if (signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty()) {
            throw new ParameterValidationFailedException("Password is required");
        }

        // Email Validation
        if (signUpRequest.getEmail() == null || signUpRequest.getEmail().isEmpty()) {
            throw new ParameterValidationFailedException("Email is required");
        } else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ParameterValidationFailedException("Email already exists");
        }

        // Username Validation
        if (signUpRequest.getUsername() != null && !signUpRequest.getUsername().isEmpty()) {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                throw new ParameterValidationFailedException("Username is already taken");
            }
        }
    }
}
