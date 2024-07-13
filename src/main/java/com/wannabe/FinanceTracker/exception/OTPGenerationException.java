package com.wannabe.FinanceTracker.exception;

import com.wannabe.FinanceTracker.model.ErrorCode;
import lombok.Data;
import lombok.Getter;


@Getter
public class OTPGenerationException extends Exception {
    private ErrorCode errorCode = ErrorCode.SERVICE_FAILED;
    public OTPGenerationException(String message) {
        super(message);
    }
}
