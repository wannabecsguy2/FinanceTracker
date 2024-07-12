package com.wannabe.FinanceTracker.utils;

import com.wannabe.FinanceTracker.exception.OTPGenerationException;
import com.wannabe.FinanceTracker.model.OTP;
import com.wannabe.FinanceTracker.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
public class OTPUtils {
    @Value("${app.otp.length}")
    private int otpLength;

    @Value("${app.otp.salt}")
    private String otpSalt;

    @Value("${app.otp.validitySec}")
    private Long otpValiditySeconds;

    public OTP generateOTP(UserPrincipal userPrincipal) throws OTPGenerationException {
        try {
            OTP otp = new OTP();

            otp.setUserId(userPrincipal.getId());
            otp.setOtp(getOtpString(otpLength));
            otp.setExpires(LocalDateTime.now().plusSeconds(otpValiditySeconds));

            return otp;
        } catch (Exception e) {
            log.error("Failed to generate otp", e);
            throw new OTPGenerationException("Failed to generate OTP");
        }
    }

    protected String getOtpString(int length) {
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();

        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * otpSalt.length());
            salt.append(otpSalt.charAt(index));
        }

        return salt.toString();
    }
}
