package com.wannabe.FinanceTracker.service;

import com.wannabe.FinanceTracker.exception.ParameterValidationFailedException;
import com.wannabe.FinanceTracker.exception.ResourceNotFoundException;
import com.wannabe.FinanceTracker.model.*;
import com.wannabe.FinanceTracker.payload.*;
import com.wannabe.FinanceTracker.repository.*;

import com.wannabe.FinanceTracker.security.authentication.OTPAuthenticationProvider;
import com.wannabe.FinanceTracker.security.authentication.PasswordAuthenticationProvider;
import com.wannabe.FinanceTracker.utils.CommonFunctionsUtils;
import com.wannabe.FinanceTracker.utils.JWTUtils;
import com.wannabe.FinanceTracker.utils.OTPUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    private PasswordAuthenticationProvider passwordAuthenticationProvider;

    @Autowired
    private OTPAuthenticationProvider otpAuthenticationProvider;

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
    private RoleRepository roleRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private SMSRepository smsRepository;

    @Autowired
    private OTPRepository otpRepository;

    @Transactional
    public GenericResponseObject<?> register(RegisterRequest registerRequest) throws Exception {
        try {
            validateSignUpRequest(registerRequest);
        } catch (ParameterValidationFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unable to register user");
        }

        switch (registerRequest.getRegistrationStep()) {
            case PHONE_REGISTERED: {
                try {
                    User user;
                    if (userRepository.existsByPhone(registerRequest.getPhone())) {
                        user = userRepository.findByEmailOrUsernameOrPhone(registerRequest.getPhone(), registerRequest.getPhone(), registerRequest.getPhone()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    } else {
                        // Create new user
                        user = new User();
                        List<Role> userRoles = new ArrayList<>();

                        Role userRole = roleRepository.findTopByOrderByLevelDesc().orElse(null);
                        if (userRole == null) {
                            throw new ResourceNotFoundException("Lowest Default Role not found");
                        }
                        userRoles.add(userRole);

                        user.setPhone(registerRequest.getPhone());

                        Country defaultCountry = countryRepository.findById(registerRequest.getDefaultCountryId()).orElseThrow(() -> new ResourceNotFoundException("Country not found"));

                        user.setDefaultCountry(defaultCountry);
                        user.setRoles(userRoles);
                        user.setRegistrationStep(RegistrationStep.PHONE_REGISTERED);


                        user = userRepository.save(user);
                    }
                    SMS sms = new SMS();
                    sms.setUserId(user.getId());
                    sms.setType(SMSType.OTP);

                    OTP otp = otpUtils.generateOTP(user.getId());
                    otp.setType(OTPType.PHONE_VERIFICATION);

                    smsService.sendVerificationSms(user, otp, sms);

                    otpRepository.save(otp);
                    smsRepository.save(sms);

                    return new GenericResponseObject<>(true, "OTP Sent, Proceed to verify Phone");
                } catch (Exception e) {
                    log.error("Exception occurred while trying to register new user", e);
                    throw new RuntimeException("Unable to register user");
                }
            }

            case PHONE_VERIFIED: {
                User user = userRepository.findByPhone(registerRequest.getPhone()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
                try {
                    if (commonFunctionsUtils.verifyOtp(registerRequest.getOtp(), user.getId(), OTPType.PHONE_VERIFICATION)) {
                        user.setRegistrationStep(RegistrationStep.PHONE_VERIFIED);
                        userRepository.save(user);

                        return new GenericResponseObject<>(true, "Phone verified successfully, proceed to enter user details");
                    } else {
                        return new GenericResponseObject<>(false, "Invalid OTP", ErrorCode.RESOURCE_NOT_FOUND);
                    }
                } catch (Exception e) {
                    log.error("Exception occurred while trying to verify phone", e);
                    throw new RuntimeException("Unable to verify phone");
                }
            }

            case USER_DETAILS_RECEIVED: {
                User user = userRepository.findByPhone(registerRequest.getPhone()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

                user.setUsername(registerRequest.getUsername());
                user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                user.setPhoneVerified(true);
                user.setRegistrationStep(RegistrationStep.USER_VERIFIED);

                user = userRepository.save(user);

                Authentication authentication = passwordAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), registerRequest.getPassword(), user.getRoles()));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                String jwt = jwtUtils.createJWT(authentication);

                return new GenericResponseObject<>(true, "User registered and logged in successfully", jwt);
            }

            default:
                throw new Exception("Invalid registration step");
        }
    }

    public GenericResponseObject<?> loginPassword(LoginPasswordRequest loginPasswordRequest) throws Exception {
        log.info("Received login password request for user with email/username/phone: {}/{}/{}", loginPasswordRequest.getEmail(), loginPasswordRequest.getUsername(), loginPasswordRequest.getPhone());
        String userIdentifier = getUserIdentifier(loginPasswordRequest);

        if(userIdentifier == null || userIdentifier.isEmpty()) {
            log.error("User identifier is null or empty");
            throw new ParameterValidationFailedException("User identifier is empty", ErrorCode.EMPTY_FIELD);
        }

        User user = userRepository.findByEmailOrUsernameOrPhone(userIdentifier, userIdentifier, userIdentifier).orElseThrow(() -> new ResourceNotFoundException("User not found with specified identifier"));

        Authentication authentication = passwordAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), loginPasswordRequest.getPassword(), user.getRoles()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.createJWT(authentication);

        return new GenericResponseObject<>(true, "Login successful", jwt);
    }

    @Transactional
    public GenericResponseObject<?> loginPhoneOtp(LoginPhoneOTPRequest loginPhoneOTPRequest) throws Exception {
        switch (loginPhoneOTPRequest.getType()) {
            case SEND: {
                log.info("Send Login OTP request received for phone: {}", loginPhoneOTPRequest.getPhone());
                if(!userRepository.existsByPhoneAndPhoneVerified(loginPhoneOTPRequest.getPhone(), true)) {
                    return new GenericResponseObject<>(false, "User not found");
                }

                User user = userRepository.findByPhone(loginPhoneOTPRequest.getPhone()).orElseThrow(() -> new ResourceNotFoundException("User not found with specified identifier"));
                if (!user.isPhoneVerified()) {
                    return new GenericResponseObject<>(false, "Login using password to verify phone first", ErrorCode.NOT_VERIFIED);
                }

                try {
                    SMS sms = new SMS();
                    sms.setUserId(user.getId());
                    sms.setType(SMSType.OTP);

                    OTP otp = otpUtils.generateOTP(user.getId());
                    otp.setType(OTPType.LOGIN);

                    smsService.sendVerificationSms(user, otp, sms);

                    otpRepository.save(otp);
                    smsRepository.save(sms);

                    return new GenericResponseObject<>(true, "OTP sent, proceed to login with OTP");
                } catch (Exception e) {
                    log.error("Exception occurred while trying to register new user", e);
                    throw new RuntimeException("Unable to send OTP at this time");
                }
            }

            case VERIFY: {
                log.info("Verify Login OTP request received for phone: {}", loginPhoneOTPRequest.getPhone());
                User user = userRepository.findByPhone(loginPhoneOTPRequest.getPhone()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
                try {
                    Authentication authentication = otpAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(user.getPhone(), loginPhoneOTPRequest.getOtp(), user.getRoles()));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    String jwt = jwtUtils.createJWT(authentication);

                    return new GenericResponseObject<>(true, "User logged in successfully", jwt);
                } catch (AuthenticationException e) {
                    throw new BadCredentialsException("Invalid OTP");
                } catch (Exception e) {
                    log.error("Exception occurred while trying to verify phone", e);
                    throw new RuntimeException("Unable to verify phone");
                }
            }

            default:
                throw new Exception("Invalid registration step");
        }
    }

    public GenericResponseObject<?> isUsernameTaken(String username) throws Exception {
        return new GenericResponseObject<>(true, "Username validation done", userRepository.existsByUsername(username));
    }

    private static String getUserIdentifier(LoginPasswordRequest loginPasswordRequest) {
        String userIdentifier = null;
        if (loginPasswordRequest.getUsername() != null && !loginPasswordRequest.getUsername().isEmpty()) {
            userIdentifier = loginPasswordRequest.getUsername();
        } else if (loginPasswordRequest.getEmail() != null && !loginPasswordRequest.getEmail().isEmpty()) {
            userIdentifier = loginPasswordRequest.getEmail();
        } else if (loginPasswordRequest.getPhone() != null && !loginPasswordRequest.getPhone().isEmpty()) {
            userIdentifier = loginPasswordRequest.getPhone();
        }
        return userIdentifier;
    }

    // TODO: Pick up Register Process from any step
    private void validateSignUpRequest(RegisterRequest registerRequest) throws Exception {

        switch (registerRequest.getRegistrationStep()) {
            case PHONE_REGISTERED: {
                commonFunctionsUtils.validatePhoneFormat(registerRequest.getPhone());

                if (userRepository.existsByPhoneAndPhoneVerified(registerRequest.getPhone(), true)) {
                    throw new ParameterValidationFailedException("Phone already exists", ErrorCode.ALREADY_EXISTS);
                }

                break;
            }

            case USER_DETAILS_RECEIVED: {
                if (registerRequest.getUsername() == null || registerRequest.getUsername().isEmpty()) {
                    throw new ParameterValidationFailedException("Username is required", ErrorCode.EMPTY_FIELD);
                } else if (userRepository.existsByUsername(registerRequest.getUsername())) {
                    throw new ParameterValidationFailedException("Username is already taken", ErrorCode.ALREADY_EXISTS);
                }

                validatePassword(registerRequest.getPassword());

                break;
            }
        }
    }

    // TODO: Password Verification
    private static void validatePassword(String password) throws Exception {
        if (password == null || password.isEmpty()) {
            throw new ParameterValidationFailedException("Password is required", ErrorCode.EMPTY_FIELD);
        }
    }
}
