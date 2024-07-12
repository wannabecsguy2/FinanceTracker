package com.wannabe.FinanceTracker.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class AssignRoleRequest {
    @JsonProperty(required=true)
    private UUID userId;
    @JsonProperty(required=true)
    private Long roleId;
}
