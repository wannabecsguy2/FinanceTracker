package com.wannabe.FinanceTracker.utils;

import com.wannabe.FinanceTracker.exception.ParameterValidationFailedException;
import com.wannabe.FinanceTracker.exception.ResourceNotFoundException;
import com.wannabe.FinanceTracker.model.*;
import com.wannabe.FinanceTracker.payload.AddTransactionRequest;
import com.wannabe.FinanceTracker.payload.RegisterRequest;
import com.wannabe.FinanceTracker.payload.UpdateProfileRequest;
import com.wannabe.FinanceTracker.payload.UpdateTransactionRequest;
import com.wannabe.FinanceTracker.repository.CountryRepository;
import com.wannabe.FinanceTracker.repository.OTPRepository;
import com.wannabe.FinanceTracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CommonFunctionsUtils {
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPRepository otpRepository;

    public void mapUserProfile(UserProfile userProfile, UpdateProfileRequest request) {
        userProfile.setFirstName(request.getFirstName());
        userProfile.setLastName(request.getLastName());
        userProfile.setBirthDate(request.getBirthDate());
    }

    public void mapUserProfile(UserProfile userProfile, RegisterRequest request) {
        userProfile.setFirstName(request.getFirstName());
        userProfile.setLastName(request.getLastName());
        userProfile.setBirthDate(request.getBirthDate());
    }

    public void mapTransaction(Transaction transaction, AddTransactionRequest addTransactionRequest, Currency currency, TransactionTag tag, TransactionMethod method, CounterParty counterParty) {
        transaction.setCreatedBy(addTransactionRequest.getCreatedBy());
        transaction.setAmount(addTransactionRequest.getAmount());
        transaction.setNote(addTransactionRequest.getNote());
        transaction.setDirection(addTransactionRequest.getDirection());
        transaction.setCurrency(currency);
        transaction.setTag(tag);
        transaction.setMethod(method);
        transaction.setCounterParty(counterParty);
        transaction.setDate(addTransactionRequest.getDate());
    }

    public void mapTransaction(Transaction existingTransaction , UpdateTransactionRequest updatedTransaction, Currency currency, TransactionTag tag, TransactionMethod method, CounterParty counterParty) {
        existingTransaction.setAmount(updatedTransaction.getAmount());
        existingTransaction.setNote(updatedTransaction.getNote());
        existingTransaction.setDirection(updatedTransaction.getDirection());
        existingTransaction.setCurrency(currency);
        existingTransaction.setTag(tag);
        existingTransaction.setMethod(method);
        existingTransaction.setCounterParty(counterParty);
        existingTransaction.setDate(updatedTransaction.getDate());
    }

    public void validatePhoneFormat(String phone) throws Exception {
        if (phone == null || phone.isEmpty()) {
            throw new ParameterValidationFailedException("Phone is required", ErrorCode.EMPTY_FIELD);
        }

        String[] phoneBreak = phone.split("-");
        if (phoneBreak.length != 2) {
            throw new ParameterValidationFailedException("Phone number in invalid format", ErrorCode.FIELD_NOT_VALID);
        }
        String extension = phoneBreak[0];

        // Extension Validation
        if (extension == null || extension.isEmpty()) {
            throw new ParameterValidationFailedException("Extension is required", ErrorCode.EMPTY_FIELD);
        } else if (!countryRepository.existsByExtension(extension)) {
            throw new ParameterValidationFailedException("Extension is invalid", ErrorCode.FIELD_NOT_VALID);
        }

        // TODO: Phone Validation
    }

    public Boolean verifyOtp(String otp, UUID userId, OTPType type) throws Exception {
        List<OTP> otpList = otpRepository.findAllByUserIdAndOtpAndVerifiedAndTypeAndExpiresAfter(userId, otp, false, type, LocalDateTime.now()).orElseThrow(() -> new ResourceNotFoundException("OTPs not found"));

        if (otpList.isEmpty()) {
            throw new ResourceNotFoundException("OTP not found");
        }

        for (OTP otpItem : otpList) {
            if (otpItem.getOtp().equals(otp) && otpItem.getExpires().isAfter(LocalDateTime.now()) && !otpItem.isVerified()) {
                otpItem.setVerified(true);
                otpRepository.save(otpItem);

                return true;
            }
        }

        return false;
    }
}
