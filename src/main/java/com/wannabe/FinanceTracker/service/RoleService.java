package com.wannabe.FinanceTracker.service;

import com.wannabe.FinanceTracker.model.Role;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    public GenericResponseObject<Collection<Role>> fetchAll() {
        return new GenericResponseObject<>(true, "Returning all roles", roleRepository.findAll());
    }
}
