package com.wannabe.FinanceTracker.service;

import com.wannabe.FinanceTracker.exception.ParameterValidationFailedException;
import com.wannabe.FinanceTracker.exception.ResourceNotFoundException;
import com.wannabe.FinanceTracker.model.*;
import com.wannabe.FinanceTracker.payload.AssignRoleRequest;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.payload.OTPVerificationRequest;
import com.wannabe.FinanceTracker.payload.UpdateProfileRequest;
import com.wannabe.FinanceTracker.repository.*;
import com.wannabe.FinanceTracker.security.UserPrincipal;
import com.wannabe.FinanceTracker.utils.CommonFunctionsUtils;
import com.wannabe.FinanceTracker.utils.OTPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    private EmailService emailService;

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
    private OTPRepository otpRepository;

    @Autowired
    private EmailRepository emailRepository;

    public GenericResponseObject<?> updateRole(AssignRoleRequest assignRoleRequest) throws Exception {
        User user = userRepository.findById(assignRoleRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", assignRoleRequest.getUserId()));

        log.info("Received assign role request for user with email: " + user.getEmail());

        Role newRole = roleRepository.findById(assignRoleRequest.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("Role", "id", assignRoleRequest.getRoleId()));
        List<Role> newRoles = roleRepository.findByLevelGreaterThanEqual(newRole.getLevel());

        user.setRoles(newRoles);

        try {
            userRepository.save(user);
            return new GenericResponseObject<>(true, "Role assigned successfully");
        } catch (Exception e) {
            log.error("Exception occurred while trying to assign role", e);
            return new GenericResponseObject<>(false, "Unable to assign role", ErrorCode.SERVICE_FAILED);
        }
    }

    public GenericResponseObject<?> isEmailVerified(UserPrincipal userPrincipal) throws Exception {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.isEmailVerified()) {
            return new GenericResponseObject<>(true, "Email verified");
        } else {
            return new GenericResponseObject<>(false, "Email not verified", ErrorCode.NOT_VERIFIED);
        }
    }

    public GenericResponseObject<?> sendVerificationEmail(UserPrincipal userPrincipal) throws Exception {
        Email verificationEmail = new Email();
        verificationEmail.setUserId(userPrincipal.getId());
        verificationEmail.setType(EmailType.OTP);

        OTP otp = otpUtils.generateOTP(userPrincipal);
        otp.setType(OTPType.EMAIL_VERIFICATION);
        try {
            emailService.sendVerificationEmail(userPrincipal, otp, verificationEmail);
            verificationEmail.setStatus(EmailStatus.SENT);

            otpRepository.save(otp);
            emailRepository.save(verificationEmail);

            log.info("Email sent successfully to user with email: " + userPrincipal.getEmail());

            return new GenericResponseObject<>(true, "Email sent successfully");
        } catch (MailException e) {
            log.error("Failed to send email due to exception:", e);

            verificationEmail.setStatus(EmailStatus.FAILED);
            emailRepository.save(verificationEmail);

            otp.setExpires(LocalDateTime.now().minusDays(1));
            otpRepository.save(otp);

            throw new Exception("Failed to send email to user");
        } catch (Exception e) {
            log.error("Verification email not set/generated/saved successfully:", e);

            verificationEmail.setStatus(EmailStatus.FAILED);
            emailRepository.save(verificationEmail);

            otp.setExpires(LocalDateTime.now().minusDays(1));
            otpRepository.save(otp);

            throw new Exception("Failed to send email to user");
        }
    }

    public GenericResponseObject<?> verifyOtp(UserPrincipal userPrincipal, OTPVerificationRequest otpVerificationRequest) throws Exception {
        String otp = otpVerificationRequest.getOtp();
        OTPType type = otpVerificationRequest.getType();

        log.info("Received otp verification request for otp: " + otp);

        if (otp == null || otp.isEmpty()) {
            log.error("OTP is null or empty");
            throw new ParameterValidationFailedException("OTP is required", ErrorCode.EMPTY_FIELD);
        }

        List<OTP> userOtpList = otpRepository.findByUserIdAndOtpAndVerifiedAndType(userPrincipal.getId(), otp, false, type).orElseThrow(() -> new ResourceNotFoundException("OTP", "otp", otp));

        for (OTP userOtp: userOtpList) {
            if (userOtp.getOtp().equals(otp) && LocalDateTime.now().isBefore(userOtp.getExpires())) {
                handleVerifiedOtp(userOtp);

                userOtp.setVerified(true);
                userOtp.setExpires(LocalDateTime.now());

                otpRepository.save(userOtp);

                return new GenericResponseObject<>(true, "OTP verified successfully");
            }
        }

        return new GenericResponseObject<>(false, "OTP is invalid", ErrorCode.FIELD_NOT_VALID);
    }

    public GenericResponseObject<?> updateProfile(UserPrincipal userPrincipal, UpdateProfileRequest updateProfileRequest) throws Exception {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        UserProfile userProfile = user.getUserProfile();

        commonFunctionsUtils.mapUserProfile(userProfile, updateProfileRequest);

        userProfileRepository.save(userProfile);

        return new GenericResponseObject<>(true, "Profile updated successfully");
    }

    public GenericResponseObject<?> updateEmail(UserPrincipal userPrincipal, String newEmail) throws Exception {
        boolean emailExists = userRepository.existsByEmail(newEmail);
        if (emailExists) {
            return new GenericResponseObject<>(false, "Email already registered, please login", ErrorCode.ALREADY_EXISTS);
        }

        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        if (user.getEmail().equals(newEmail)) {
            return new GenericResponseObject<>(false, "Cannot change to existing email", ErrorCode.FIELD_NOT_VALID);
        }

        user.setEmail(newEmail);
        user.setEmailVerified(false);

        return new GenericResponseObject<>(true, "Email updated successfully");
    }

    public void handleVerifiedOtp(OTP otp) throws Exception {
        switch (otp.getType()) {
            case EMAIL_VERIFICATION: {
                User user = userRepository.findById(otp.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", otp.getUserId()));
                user.setEmailVerified(true);
                userRepository.save(user);
                break;
            }
            default: {
                throw new Exception("OTP Type is not found");
            }
        }
    }
}
