package com.wannabe.FinanceTracker.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SignUpRequest {
    @JsonProperty(required=true)
    private String phone; // Format: {Extension}-{Number}
    @JsonProperty(required=true)
    private String email;
    @JsonProperty(required=true)
    private String password;
    @JsonProperty(required=true)
    private String firstName;
    @JsonProperty(required=true)
    private String lastName;
    @JsonProperty(required=true)
    private String username;
    @JsonProperty(required=true)
    private Date birthDate;
}
