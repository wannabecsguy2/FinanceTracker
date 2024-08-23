package com.wannabe.FinanceTracker.exception;

import com.wannabe.FinanceTracker.model.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParameterValidationFailedException extends RuntimeException {
    private String message;
    private ErrorCode errorCode = ErrorCode.FIELD_NOT_VALID;

    public ParameterValidationFailedException(String message, ErrorCode errorCode) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }
}
