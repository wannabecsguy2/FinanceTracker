package com.wannabe.FinanceTracker.controller;

import com.wannabe.FinanceTracker.exception.ResourceNotFoundException;
import com.wannabe.FinanceTracker.model.ErrorCode;
import com.wannabe.FinanceTracker.model.Transaction;
import com.wannabe.FinanceTracker.payload.AddTransactionRequest;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.payload.TransactionFetchFilter;
import com.wannabe.FinanceTracker.payload.UpdateTransactionRequest;
import com.wannabe.FinanceTracker.security.UserPrincipal;
import com.wannabe.FinanceTracker.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${app.base.url}/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public ResponseEntity<GenericResponseObject<?>> add(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody AddTransactionRequest addTransactionRequest) {
        try {
            return ResponseEntity.ok().body(transactionService.add(userPrincipal, addTransactionRequest));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, e.getMessage(), e.getErrorCode()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Exception occurred while adding transaction", ErrorCode.SERVICE_FAILED));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update")
    public ResponseEntity<GenericResponseObject<?>> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody UpdateTransactionRequest transaction) {
        try {
            return ResponseEntity.ok().body(transactionService.update(userPrincipal, transaction));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, e.getMessage(), e.getErrorCode()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Exception occurred while updating transaction", ErrorCode.SERVICE_FAILED));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete")
    public ResponseEntity<GenericResponseObject<?>> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam(name="id") UUID transactionId) {
        try {
            return ResponseEntity.ok().body(transactionService.delete(userPrincipal, transactionId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, e.getMessage(), e.getErrorCode()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Exception occurred while deleting transaction", ErrorCode.SERVICE_FAILED));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/batch-delete")
    public ResponseEntity<GenericResponseObject<?>> batchDelete(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody List<UUID> transactionIds) {
        try {
            return ResponseEntity.ok().body(transactionService.batchDelete(userPrincipal, transactionIds));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, e.getMessage(), e.getErrorCode()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Exception occurred while deleting transaction", ErrorCode.SERVICE_FAILED));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/fetch")
    public ResponseEntity<GenericResponseObject<?>> fetch(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody TransactionFetchFilter filter, @RequestParam(name="PageNumber") int pageNumber, @RequestParam(name="PageSize") int pageSize) {
        try {
            return ResponseEntity.ok().body(transactionService.fetch(userPrincipal, filter, pageNumber, pageSize));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, e.getMessage(), e.getErrorCode()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Exception occurred while fetching transaction", ErrorCode.SERVICE_FAILED));
        }
    }
    // TODO: Add endpoint sends notification (in-app/email) depending on counter party type and option to user
}
