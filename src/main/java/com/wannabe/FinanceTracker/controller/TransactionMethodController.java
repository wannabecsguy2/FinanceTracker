package com.wannabe.FinanceTracker.controller;

import com.wannabe.FinanceTracker.model.ErrorCode;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.service.TransactionMethodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${app.base.url}/transaction-method")
public class TransactionMethodController {
    @Autowired
    private TransactionMethodService transactionMethodService;

    @GetMapping("/fetch-all")
    public ResponseEntity<GenericResponseObject<?>> fetchAll() {
        try {
            return ResponseEntity.ok().body(transactionMethodService.fetchAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Exception occurred while fetching transaction methods", ErrorCode.SERVICE_FAILED));
        }
    }
}
