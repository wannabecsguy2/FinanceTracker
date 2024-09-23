package com.wannabe.FinanceTracker.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wannabe.FinanceTracker.model.Currency;
import com.wannabe.FinanceTracker.model.TransactionDirection;
import com.wannabe.FinanceTracker.model.TransactionMethod;
import com.wannabe.FinanceTracker.model.TransactionTag;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddTransactionRequest {
    private UUID createdBy;
    private UUID counterPartyId;
    private TransactionDirection direction;
    private Long tagId;
    private Long methodId;
    private BigDecimal amount;
    private LocalDateTime date;
    private Long currencyId;
    private String note;
}
