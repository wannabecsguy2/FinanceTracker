package com.wannabe.FinanceTracker.webhook;

import com.twilio.security.RequestValidator;
import com.wannabe.FinanceTracker.exception.ResourceNotFoundException;
import com.wannabe.FinanceTracker.model.SMS;
import com.wannabe.FinanceTracker.model.SMSStatus;
import com.wannabe.FinanceTracker.repository.SMSRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("${app.base.url}" + "${twilio.webhook.baseEndpoint}")
public class TwilioWebhook {
    // TODO: Debug
    // @Value("${twilio.account.authToken}")
    private String twilioAccountAuthToken = "sdbjahbsdbaksndkanksdn";

    private SMSRepository smsRepository;

    private final RequestValidator requestValidator = new RequestValidator(twilioAccountAuthToken);

    @PostMapping("${twilio.webhook.statusCallbackEndpoint}")
    public void statusCallbackWebhook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String messageSID = request.getParameter("MessageSid");
        String messageStatus = request.getParameter("MessageStatus");
        SMSStatus smsStatus;
        try {
            smsStatus = SMSStatus.valueOf(messageStatus.toUpperCase());
        } catch (Exception e) {
            log.error("Could not parse {} in SMSStatus enum", messageStatus);
            smsStatus = SMSStatus.UNHANDLED;
        }

        SMS sms = smsRepository.findBySid(messageSID).orElseThrow(() -> new ResourceNotFoundException("Message with specified SID not found"));

        // TODO: Improve handling the Status
        switch (smsStatus) {
            default:
                sms.setStatus(smsStatus);
                smsRepository.save(sms);
                break;
        }

        response.setStatus(204);
    }
}
