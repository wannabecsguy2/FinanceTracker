package com.wannabe.FinanceTracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParameterValidationFailedException extends RuntimeException {
    private String message;
    private String response;

    public ParameterValidationFailedException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public ParameterValidationFailedException(String message, String response) {
        super(message);
        this.message = message;
        this.response = response;
    }

    public ParameterValidationFailedException(String message) {
        super(message);
        this.response = message;
    }
}
