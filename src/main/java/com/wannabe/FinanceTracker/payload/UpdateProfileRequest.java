package com.wannabe.FinanceTracker.payload;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateProfileRequest {
    private String firstName;
    private String lastName;
    private Date birthDate;
}
