package com.wannabe.FinanceTracker.service;

import com.wannabe.FinanceTracker.exception.ParameterValidationFailedException;
import com.wannabe.FinanceTracker.exception.ResourceNotFoundException;
import com.wannabe.FinanceTracker.model.*;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.payload.LoginPhoneOTPRequest;
import com.wannabe.FinanceTracker.payload.LoginPasswordRequest;
import com.wannabe.FinanceTracker.payload.SignUpRequest;
import com.wannabe.FinanceTracker.repository.*;

import com.wannabe.FinanceTracker.utils.CommonFunctionsUtils;
import com.wannabe.FinanceTracker.utils.JWTUtils;
import com.wannabe.FinanceTracker.utils.OTPUtils;
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
    private SMSService smsService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private OTPUtils otpUtils;

    @Autowired
    private CommonFunctionsUtils commonFunctionsUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Transactional
    public GenericResponseObject<?> signUp(SignUpRequest signUpRequest) throws Exception {
        try {
            validateSignUpRequest(signUpRequest);
        } catch (ParameterValidationFailedException e) {
            throw e;
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
        user.setPhone(signUpRequest.getPhone());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRoles(userRoles);

        Country defaultCountry = countryRepository.findById(signUpRequest.getDefaultCountryId()).orElseThrow(() -> new ResourceNotFoundException("Country not found"));
        user.setDefaultCountry(defaultCountry);

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

    public GenericResponseObject<?> loginPassword(LoginPasswordRequest loginPasswordRequest) throws Exception {
        log.info("Received login password request for user with email/username/phone: " + loginPasswordRequest.getEmail() + "/" + loginPasswordRequest.getUsername() + "/" + loginPasswordRequest.getPhone());
        // TODO: User Identifier Code is not working
        String userIdentifier = null;
        if (loginPasswordRequest.getUsername() != null && !loginPasswordRequest.getUsername().isEmpty()) {
            userIdentifier = loginPasswordRequest.getUsername();
        } else if (loginPasswordRequest.getEmail() != null && !loginPasswordRequest.getEmail().isEmpty()) {
            userIdentifier = loginPasswordRequest.getEmail();
        } else if (loginPasswordRequest.getPhone() != null && !loginPasswordRequest.getPhone().isEmpty()) {
            userIdentifier = loginPasswordRequest.getPhone();
        }

        if(userIdentifier == null || userIdentifier.isEmpty()) {
            log.error("User identifier is null or empty");
            throw new ParameterValidationFailedException("User identifier is empty", ErrorCode.EMPTY_FIELD);
        }

        User user = userRepository.findByEmailOrUsernameOrPhone(userIdentifier, userIdentifier, userIdentifier).orElseThrow(() -> new ResourceNotFoundException("User not found with specified identifier"));

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), loginPasswordRequest.getPassword(), user.getRoles()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.createJWT(authentication);

        return new GenericResponseObject<>(true, jwt, user);
    }

    public GenericResponseObject<?> loginPhoneOtp(LoginPhoneOTPRequest loginPhoneOTPRequest) throws Exception {
        switch (loginPhoneOTPRequest.getType()) {
            case SEND: {
                if(!userRepository.existsByPhone(loginPhoneOTPRequest.getPhone())) {
                    return new GenericResponseObject<>(false, "User not found");
                }

                User user = userRepository.findByPhone(loginPhoneOTPRequest.getPhone()).orElseThrow(() -> new ResourceNotFoundException("User not found with specified identifier"));
                if (!user.isPhoneVerified()) {
                    return new GenericResponseObject<>(false, "Login using password to verify phone first", ErrorCode.NOT_VERIFIED);
                }

                SMS sms = new SMS();
                sms.setUserId(user.getId());
                sms.setType(SMSType.OTP);

                OTP otp = otpUtils.generateOTP(user.getId());
                otp.setType(OTPType.LOGIN);
                try {
                    smsService.sendLoginSms(user, otp, sms);
                } catch (Exception e) {
                    return new GenericResponseObject<>(false, "SMS could not be sent");
                }
            }

            case VERIFY: {

            }

            default: {

            }
        }
        // TODO: Put token in message and try to configure both email and Phone in one
        return new GenericResponseObject<>(false, "Service has not been implemented", ErrorCode.METHOD_NOT_IMPLEMENTED);
    }

    public GenericResponseObject<?> isUsernameTaken(String username) {
        return new GenericResponseObject<>(true, "Username validation done", userRepository.existsByUsername(username));
    }

    public GenericResponseObject<?> fetchAllCountries() {
        return new GenericResponseObject<>(true, "Countries fetch", countryRepository.findAll());
    }

    private void validateSignUpRequest(SignUpRequest signUpRequest) throws ParameterValidationFailedException {
        // Password Validation
        if (signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty()) {
            throw new ParameterValidationFailedException("Password is required", ErrorCode.EMPTY_FIELD);
        }

        // TODO: Email Validation
        if (signUpRequest.getEmail() == null || signUpRequest.getEmail().isEmpty()) {
            throw new ParameterValidationFailedException("Email is required", ErrorCode.EMPTY_FIELD);
        } else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ParameterValidationFailedException("Email already exists", ErrorCode.ALREADY_EXISTS);
        }

        // Username Validation
        if (signUpRequest.getUsername() == null || signUpRequest.getUsername().isEmpty()) {
            throw new ParameterValidationFailedException("Username is required", ErrorCode.EMPTY_FIELD);
        } else if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ParameterValidationFailedException("Username is already taken", ErrorCode.ALREADY_EXISTS);
        }

        String[] phoneBreak = signUpRequest.getPhone().split("-");
        if (phoneBreak.length != 2) {
            throw new ParameterValidationFailedException("Phone number in invalid format", ErrorCode.FIELD_NOT_VALID);
        }
        String extension = phoneBreak[0];

        // Extension Validation
        if (extension == null || extension.isEmpty()) {
            throw new ParameterValidationFailedException("Extension is required", ErrorCode.EMPTY_FIELD);
        } else if (!countryRepository.existsByExtension(extension)) {
            throw new ParameterValidationFailedException("Extension is invalid", ErrorCode.FIELD_NOT_VALID);
        }

        // TODO: Phone Validation
        if (userRepository.existsByPhone(signUpRequest.getPhone())) {
            throw new ParameterValidationFailedException("Phone number already in use", ErrorCode.ALREADY_EXISTS);
        }
    }
}
