package com.wannabe.FinanceTracker.controller;

import com.wannabe.FinanceTracker.model.ErrorCode;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("${app.base.url}/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/fetch-all")
    public ResponseEntity<GenericResponseObject<?>> fetchAll() {
        try {
            return ResponseEntity.ok().body(roleService.fetchAll());
        } catch (Exception e) {
            log.error("Exception occurred while trying to fetch roles", e);
            return ResponseEntity.internalServerError().body(new GenericResponseObject<>(false, "Failed to fetch roles", ErrorCode.SERVICE_FAILED));
        }
    }
}
