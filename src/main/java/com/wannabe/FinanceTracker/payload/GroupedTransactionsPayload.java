package com.wannabe.FinanceTracker.payload;

import com.wannabe.FinanceTracker.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface GroupedTransactionsPayload {
    public String getGroupLabel();

    public List<Transaction> getTransactions();

    public BigDecimal getNetTransactionAmount();
}
