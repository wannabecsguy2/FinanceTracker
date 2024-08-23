package com.wannabe.FinanceTracker.controller;

import com.wannabe.FinanceTracker.model.Currency;
import com.wannabe.FinanceTracker.model.ErrorCode;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("${app.base.url}/currency")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<GenericResponseObject<?>> add(@RequestBody Currency newCurrency) {
        try {
            return ResponseEntity.ok().body(currencyService.add(newCurrency));
        } catch (Exception e) {
            log.error("Exception occurred while adding new currency", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Could not add currency", ErrorCode.SERVICE_FAILED));
        }
    }

    @GetMapping("/fetch-all")
    public ResponseEntity<GenericResponseObject<?>> fetchAll() {
        try {
            return ResponseEntity.ok().body(currencyService.fetchAll());
        } catch (Exception e) {
            log.error("Exception occurred while returning currencies", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Error while fetching currencies", ErrorCode.SERVICE_FAILED));
        }
    }
}
