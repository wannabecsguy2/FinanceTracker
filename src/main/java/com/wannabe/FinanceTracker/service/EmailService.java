package com.wannabe.FinanceTracker.service;

import com.wannabe.FinanceTracker.model.Email;
import com.wannabe.FinanceTracker.model.EmailStatus;
import com.wannabe.FinanceTracker.model.OTP;
import com.wannabe.FinanceTracker.security.UserPrincipal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendVerificationEmail(UserPrincipal userPrincipal, OTP otp, Email email) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userPrincipal.getEmail());
        message.setSubject("OTP for Account Verification");
        message.setText("Your OTP for account verification is: " + otp.getOtp());

        javaMailSender.send(message);
    }
}
