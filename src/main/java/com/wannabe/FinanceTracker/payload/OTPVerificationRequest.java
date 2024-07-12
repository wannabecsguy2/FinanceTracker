package com.wannabe.FinanceTracker.payload;

import com.wannabe.FinanceTracker.model.OTPType;
import lombok.Data;

@Data
public class OTPVerificationRequest {
    private String otp;
    private OTPType type;
}
