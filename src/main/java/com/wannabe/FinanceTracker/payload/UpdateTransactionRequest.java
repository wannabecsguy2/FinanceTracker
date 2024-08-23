package com.wannabe.FinanceTracker.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wannabe.FinanceTracker.model.TransactionDirection;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateTransactionRequest {
    private UUID id;
    private UUID counterPartyId;
    private TransactionDirection direction;
    private Long tagId;
    private Long methodId;
    private BigDecimal amount;
    private Long currencyId;
    private String note;
}
