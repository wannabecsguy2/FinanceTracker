package com.wannabe.FinanceTracker.service;

import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.repository.TransactionMethodRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionMethodService {
    @Autowired
    private TransactionMethodRepository transactionMethodRepository;

    public GenericResponseObject<?> fetchAll() throws Exception {
        return new GenericResponseObject<>(true, "Fetched all transaction methods", transactionMethodRepository.findAll());
    }
}
