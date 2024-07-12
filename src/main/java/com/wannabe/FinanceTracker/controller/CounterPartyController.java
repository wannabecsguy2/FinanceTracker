package com.wannabe.FinanceTracker.controller;

import com.wannabe.FinanceTracker.model.CounterParty;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.security.UserPrincipal;
import com.wannabe.FinanceTracker.service.CounterPartyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${app.base.url}/counter-party")
public class CounterPartyController {
    @Autowired
    private CounterPartyService counterPartyService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add-counter-party")
    public ResponseEntity<GenericResponseObject<?>> addCounterParty(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CounterParty addCounterPartyRequest) {
        try {
            return ResponseEntity.ok().body(counterPartyService.addCounterParty());
        } catch (Exception e) {
            log.error("Exception occurred while adding CounterParty", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Failed to add counter party"));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/fetch-all-types")
    public ResponseEntity<GenericResponseObject<?>> fetchAllTypes() {
        try {
            return ResponseEntity.ok().body(counterPartyService.fetchAllTypes());
        } catch (Exception e) {
            log.error("Exception occurred while fetching all counter party types", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Internal Server Error, please try again later"));
        }
    }






    // TODO: Add endpoints for add, search, delete and update if counter party registers (By email)

    // TODO: Figure out approval for being added as a counter party and approving the specified counter party if it registers on the app. Add requests mode also.

    // TODO: Add endpoint sends notification of request if user already exists. Add endpoint to also check if email is not null for CounterParty of certain types (FRIENDS, FAMILY)
}
