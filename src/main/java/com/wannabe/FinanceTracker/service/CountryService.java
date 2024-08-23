package com.wannabe.FinanceTracker.service;

import com.wannabe.FinanceTracker.model.Country;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.repository.CountryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;

    public GenericResponseObject<List<Country>> fetchAll() throws Exception {
        return new GenericResponseObject<>(true, "Returning all countries", countryRepository.findAll());
    }
}
