package com.wannabe.FinanceTracker.utils;

import com.wannabe.FinanceTracker.model.*;
import com.wannabe.FinanceTracker.payload.AddTransactionRequest;
import com.wannabe.FinanceTracker.payload.SignUpRequest;
import com.wannabe.FinanceTracker.payload.UpdateProfileRequest;
import com.wannabe.FinanceTracker.payload.UpdateTransactionRequest;
import org.springframework.stereotype.Service;

@Service
public class CommonFunctionsUtils {
    public void mapUserProfile(UserProfile userProfile, UpdateProfileRequest request) {
        userProfile.setFirstName(request.getFirstName());
        userProfile.setLastName(request.getLastName());
        userProfile.setBirthDate(request.getBirthDate());
    }

    public void mapUserProfile(UserProfile userProfile, SignUpRequest request) {
        userProfile.setFirstName(request.getFirstName());
        userProfile.setLastName(request.getLastName());
        userProfile.setBirthDate(request.getBirthDate());
    }

    public void mapTransaction(Transaction transaction, AddTransactionRequest addTransactionRequest, Currency currency, TransactionTag tag, TransactionMethod method) {
        transaction.setAmount(addTransactionRequest.getAmount());
        transaction.setNote(addTransactionRequest.getNote());
        transaction.setDirection(addTransactionRequest.getDirection());
        transaction.setCurrency(currency);
        transaction.setTag(tag);
        transaction.setMethod(method);
        transaction.setCounterPartyId(addTransactionRequest.getCounterPartyId());
    }

    public void mapTransaction(Transaction existingTransaction , UpdateTransactionRequest updatedTransaction, Currency currency, TransactionTag tag, TransactionMethod method) {
        existingTransaction.setAmount(updatedTransaction.getAmount());
        existingTransaction.setNote(updatedTransaction.getNote());
        existingTransaction.setDirection(updatedTransaction.getDirection());
        existingTransaction.setCurrency(currency);
        existingTransaction.setTag(tag);
        existingTransaction.setMethod(method);
        existingTransaction.setCounterPartyId(updatedTransaction.getCounterPartyId());
    }
}
