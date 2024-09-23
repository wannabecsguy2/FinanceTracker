package com.wannabe.FinanceTracker.payload;

import com.wannabe.FinanceTracker.model.RegistrationStep;
import lombok.Data;

import java.util.Date;

@Data
public class RegisterRequest {
    private String phone; // Format: {Extension}-{Number}
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String username;
    private Date birthDate;
    private Long defaultCountryId;
    private RegistrationStep registrationStep;
    private String otp;

    /*
    RegistrationStep:
        PHONE_REGISTERED:
            {
                phone: "{Extension}-{Number}",
                registrationStep: PHONE_REGISTERED,
                defaultCountryId: // From Phone
            },
        PHONE_VERIFIED:
            {
                phone: "{Extension}-{Number}",
                registrationStep: PHONE_VERIFIED,
                otp: ""
            }
        USER_DETAILS_RECEIVED:
            {
                phone: "{Extension}-{Number}",
                registrationStep: USER_DETAILS_RECEIVED,
                username: "",
                password: "",
            }
     */
}
