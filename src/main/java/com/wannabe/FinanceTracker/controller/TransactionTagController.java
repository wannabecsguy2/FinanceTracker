package com.wannabe.FinanceTracker.controller;

import com.wannabe.FinanceTracker.model.ErrorCode;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.service.TransactionTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${app.base.url}/transaction-tag")
public class TransactionTagController {
    @Autowired
    private TransactionTagService transactionTagService;

    @GetMapping("/fetch-all")
    public ResponseEntity<GenericResponseObject<?>> fetchAll() {
        try {
            return ResponseEntity.ok().body(transactionTagService.fetchAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Exception occurred while fetching transaction tags", ErrorCode.SERVICE_FAILED));
        }
    }
}
