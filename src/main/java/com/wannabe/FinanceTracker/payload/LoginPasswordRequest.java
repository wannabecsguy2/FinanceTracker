package com.wannabe.FinanceTracker.payload;

import lombok.Data;

@Data
public class LoginPasswordRequest {
    private String username;
    private String email;
    private String password;
    private String phone;
}
