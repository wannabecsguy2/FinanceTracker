package com.wannabe.FinanceTracker.controller;

import com.wannabe.FinanceTracker.exception.ParameterValidationFailedException;
import com.wannabe.FinanceTracker.exception.ResourceNotFoundException;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.payload.LoginRequest;
import com.wannabe.FinanceTracker.payload.SignUpRequest;
import com.wannabe.FinanceTracker.service.AuthService;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${app.base.url}/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<GenericResponseObject<?>> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {
            return ResponseEntity.ok().body(authService.signUp(signUpRequest));
        } catch (ParameterValidationFailedException e) {
            return ResponseEntity.badRequest().body(new GenericResponseObject<>(false, e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, e.getMessage()));
        } catch (Exception e) {
            log.error("Exception occurred while trying to sign up", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Registration failed"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponseObject<?>> login(@RequestBody LoginRequest loginRequest) {
        try {
            GenericResponseObject<?> response = authService.login(loginRequest);
            return ResponseEntity.ok().body(response);
        } catch (ParameterValidationFailedException e) {
            return ResponseEntity.badRequest().body(new GenericResponseObject<>(false, e.getMessage()));
        } catch (Exception e) {
            log.error("Exception occurred while trying to login", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Login failed"));
        }
    }

    // TODO: Forgot Password
}
