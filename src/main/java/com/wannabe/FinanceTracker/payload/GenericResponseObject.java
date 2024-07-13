package com.wannabe.FinanceTracker.payload;

import com.wannabe.FinanceTracker.model.ErrorCode;
import lombok.Data;

@Data
public class GenericResponseObject<T> {
    private Boolean success;
    private String message;
    private ErrorCode errorCode;
    private boolean unhandledException;
    private T data;

    public GenericResponseObject(Boolean success, String message, ErrorCode errorCode) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
        this.data = null;
    }

    public GenericResponseObject(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public GenericResponseObject(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
