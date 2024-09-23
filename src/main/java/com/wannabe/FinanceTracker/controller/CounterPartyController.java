package com.wannabe.FinanceTracker.controller;

import com.wannabe.FinanceTracker.exception.ResourceNotFoundException;
import com.wannabe.FinanceTracker.model.CounterParty;
import com.wannabe.FinanceTracker.model.ErrorCode;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.security.UserPrincipal;
import com.wannabe.FinanceTracker.service.CounterPartyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("${app.base.url}/counter-party")
public class CounterPartyController {
    @Autowired
    private CounterPartyService counterPartyService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public ResponseEntity<GenericResponseObject<?>> add(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CounterParty addCounterPartyPayload) {
        try {
            return ResponseEntity.ok().body(counterPartyService.add(userPrincipal, addCounterPartyPayload));
        } catch (Exception e) {
            log.error("Exception occurred while adding CounterParty", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Failed to add counter party", ErrorCode.SERVICE_FAILED));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update")
    public ResponseEntity<GenericResponseObject<?>> update(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CounterParty updateCounterPartyPayload) {
        try {
            return ResponseEntity.ok().body(counterPartyService.update(userPrincipal, updateCounterPartyPayload));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, e.getMessage(), e.getErrorCode()));
        } catch (Exception e) {
            log.error("Exception occurred while updating counter party", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Failed to update counter party", ErrorCode.SERVICE_FAILED));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete")
    public ResponseEntity<GenericResponseObject<?>> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam("id") UUID id) {
        try {
            return ResponseEntity.ok().body(counterPartyService.delete(userPrincipal, id));
        }  catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, e.getMessage(), e.getErrorCode()));
        } catch (Exception e) {
            log.error("Exception occurred while deleting counter party", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Failed to delete counter party", ErrorCode.SERVICE_FAILED));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/fetch-all")
    public ResponseEntity<GenericResponseObject<?>> fetchAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            return ResponseEntity.ok().body(counterPartyService.fetchAll(userPrincipal));
        } catch (Exception e) {
            log.error("Exception occurred while fetching all counter parties", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Internal Server Error, please try again later", ErrorCode.SERVICE_FAILED));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/fetch")
    public ResponseEntity<GenericResponseObject<?>> fetch(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam("id") UUID id) {
        try {
            return ResponseEntity.ok().body(counterPartyService.fetch(userPrincipal, id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, e.getMessage(), e.getErrorCode()));
        } catch (Exception e) {
            log.error("Exception occurred while fetching counter party", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Internal Server Error, please try again later", ErrorCode.SERVICE_FAILED));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/fetch-all-types")
    public ResponseEntity<GenericResponseObject<?>> fetchAllTypes() {
        try {
            return ResponseEntity.ok().body(counterPartyService.fetchAllTypes());
        } catch (Exception e) {
            log.error("Exception occurred while fetching all counter party types", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Internal Server Error, please try again later", ErrorCode.SERVICE_FAILED));
        }
    }


    // TODO: Add endpoints for search and update if counter party registers (By email)

    // TODO: Figure out approval for being added as a counter party and approving the specified counter party if it registers on the app. Add requests mode also.

    // TODO: Add endpoint sends notification of request if user already exists. Add endpoint to also check if email is not null for CounterParty of certain types (FRIENDS, FAMILY)
}
