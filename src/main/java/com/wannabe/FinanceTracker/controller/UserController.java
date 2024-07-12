package com.wannabe.FinanceTracker.controller;

import com.wannabe.FinanceTracker.exception.OTPGenerationException;
import com.wannabe.FinanceTracker.exception.ResourceNotFoundException;
import com.wannabe.FinanceTracker.payload.AssignRoleRequest;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.payload.OTPVerificationRequest;
import com.wannabe.FinanceTracker.payload.UpdateProfileRequest;
import com.wannabe.FinanceTracker.security.UserPrincipal;
import com.wannabe.FinanceTracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${app.base.url}/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/assign-role")
    public ResponseEntity<GenericResponseObject<?>> updateRole(@RequestBody AssignRoleRequest assignRoleRequest) {
        try {
            return ResponseEntity.ok().body(userService.updateRole(assignRoleRequest));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new GenericResponseObject<>(false, e.getMessage()));
        } catch (Exception e) {
            log.error("Exception occurred while trying to assign role", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Role assignment failed"));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/send-verification-email")
    public ResponseEntity<GenericResponseObject<?>> sendVerificationEmail(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            return ResponseEntity.ok().body(userService.sendVerificationEmail(userPrincipal));
        } catch (OTPGenerationException e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "OTP could not be generated"));
        } catch (Exception e) {
            log.error("Exception occurred while trying to send verification email", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Email verification failed"));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/verify-otp")
    public ResponseEntity<GenericResponseObject<?>> verifyOtp(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody OTPVerificationRequest otpVerificationRequest) {
        try {
            return ResponseEntity.ok().body(userService.verifyOtp(userPrincipal, otpVerificationRequest));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new GenericResponseObject<>(false, e.getMessage()));
        } catch (Exception e) {
            log.error("Exception occurred while trying to verify otp", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "OTP verification failed"));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update-profile")
    public ResponseEntity<GenericResponseObject<?>> updateProfile(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody UpdateProfileRequest updateProfileRequest) {
        try {
            return ResponseEntity.ok().body(userService.updateProfile(userPrincipal, updateProfileRequest));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new GenericResponseObject<>(false, e.getMessage()));
        } catch (Exception e) {
            log.error("Exception occurred while trying to update profile", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Failed to update profile"));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update-email")
    public ResponseEntity<GenericResponseObject<?>> updateEmail(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody String newEmail) {
        try {
            return ResponseEntity.ok().body(userService.updateEmail(userPrincipal, newEmail));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new GenericResponseObject<>(false, e.getMessage()));
        } catch (Exception e) {
            log.error("Exception occurred while trying to update Email", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Failed to update email"));
        }
    }

    // TODO: Update Username

    // TODO: Update Password

    // TODO: Endpoint to check if user is registered as counter party

}
