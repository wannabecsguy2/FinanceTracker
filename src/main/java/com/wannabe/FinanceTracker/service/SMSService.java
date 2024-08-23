package com.wannabe.FinanceTracker.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.wannabe.FinanceTracker.model.OTP;
import com.wannabe.FinanceTracker.model.SMS;
import com.wannabe.FinanceTracker.model.User;
import com.wannabe.FinanceTracker.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SMSService {
    @Value("${twilio.account.sid}")
    private String twilioAccountSID;

    @Value("${twilio.account.authToken")
    private String twilioAccountAuthToken;

    @Value("${twilio.account.phone}")
    private String twilioAccountPhone;

    @Value("${app.base.url}")
    private String appBaseUrl;

    @Value("${app.public.url}")
    private String appPublicUrl;

    @Value("${twilio.webhook.baseEndpoint")
    private String twilioWebhookBaseEndpoint;

    @Value("${twilio.webhook.statusCallbackEndpoint")
    private String twilioWebhookStatusCallbackEndpoint;


    public void sendVerificationSms(UserPrincipal userPrincipal, OTP otp, SMS sms) throws Exception {

    }

    public void sendLoginSms(User user, OTP otp, SMS sms) throws Exception {
        Twilio.init(twilioAccountSID, twilioAccountAuthToken);
        Message message = Message.creator(
                new PhoneNumber("+" + user.getPhone().replace("-", "")),
                new PhoneNumber(twilioAccountPhone),
                "OTP for login is:" + otp.getOtp()
        ).setStatusCallback(appPublicUrl + appBaseUrl + twilioWebhookBaseEndpoint + twilioWebhookStatusCallbackEndpoint).create();
        sms.setSid(message.getSid());
    }
}
