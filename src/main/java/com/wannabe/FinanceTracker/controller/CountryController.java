package com.wannabe.FinanceTracker.controller;

import com.wannabe.FinanceTracker.model.ErrorCode;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${app.base.url}/country")
public class CountryController {
    @Autowired
    private CountryService countryService;

    @GetMapping("/fetch-all")
    public ResponseEntity<GenericResponseObject<?>> fetchAllCountries() {
        try {
            return ResponseEntity.ok().body(countryService.fetchAll());
        } catch (Exception e) {
            log.error("Exception occurred while trying to fetch all countries", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Could not fetch countries", ErrorCode.SERVICE_FAILED));
        }
    }
}
