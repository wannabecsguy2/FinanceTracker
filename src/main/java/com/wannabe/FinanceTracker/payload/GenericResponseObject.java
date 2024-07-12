package com.wannabe.FinanceTracker.payload;

import lombok.Data;

@Data
public class GenericResponseObject<T> {
    private Boolean success;
    private String message;
    private T data;

    public GenericResponseObject(Boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }

    public GenericResponseObject(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
