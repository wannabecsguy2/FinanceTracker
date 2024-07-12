package com.wannabe.FinanceTracker.service;

import com.wannabe.FinanceTracker.model.Currency;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    public GenericResponseObject<?> addCurrency(Currency newCurrency) throws Exception {
        if (currencyRepository.existsByCodeOrName(newCurrency.getCode(), newCurrency.getName())) {
            return new GenericResponseObject<>(false, "Currency already exists");
        } else {
            currencyRepository.save(newCurrency);
            return new GenericResponseObject<>(true, "Currency exists successfully");
        }
    }

    public GenericResponseObject<?> fetchAllCurrency() throws Exception {
        return new GenericResponseObject<>(true, "Returning all currencies", currencyRepository.findAll());
    }
}
