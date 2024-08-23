package com.wannabe.FinanceTracker.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wannabe.FinanceTracker.model.TransactionDirection;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionFetchFilter {
    private List<UUID> createdBy = new ArrayList<>();
    private BigDecimal minAmount = BigDecimal.ZERO;
    private BigDecimal maxAmount = null;
    private List<UUID> counterPartyIds = new ArrayList<>();
    private List<TransactionDirection> directions = new ArrayList<>();
    private List<Long> tagIds = new ArrayList<>();
    private List<Long> methodIds = new ArrayList<>();
    private List<Long> currencyIds = new ArrayList<>();
    private String sortBy = "created"; // "created" | "amount"
}
