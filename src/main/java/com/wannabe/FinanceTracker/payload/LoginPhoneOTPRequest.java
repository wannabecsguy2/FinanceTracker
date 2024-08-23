package com.wannabe.FinanceTracker.payload;

import com.wannabe.FinanceTracker.model.OTP;
import lombok.Data;

@Data
public class LoginPhoneOTPRequest {
    private String phone;
    private String otp;
    private LoginOTPRequestType type;
}
