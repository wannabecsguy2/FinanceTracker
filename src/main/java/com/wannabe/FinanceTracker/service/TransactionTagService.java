package com.wannabe.FinanceTracker.service;

import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.repository.TransactionTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Service
public class TransactionTagService {
    @Autowired
    private TransactionTagRepository transactionTagRepository;

    public GenericResponseObject<?> fetchAll() throws Exception {
        try {
            return new GenericResponseObject<>(true, "Fetched all transaction tags", transactionTagRepository.findAll());
        } catch (Exception e) {
            log.error("Exception occurred while fetching transaction tags", e);
            throw new RuntimeException(e);
        }
    }
}
