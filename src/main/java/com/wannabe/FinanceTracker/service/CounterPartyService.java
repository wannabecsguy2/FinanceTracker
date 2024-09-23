package com.wannabe.FinanceTracker.service;

import com.wannabe.FinanceTracker.exception.ParameterValidationFailedException;
import com.wannabe.FinanceTracker.exception.ResourceNotFoundException;
import com.wannabe.FinanceTracker.model.CounterParty;
import com.wannabe.FinanceTracker.model.CounterPartyType;
import com.wannabe.FinanceTracker.model.ErrorCode;
import com.wannabe.FinanceTracker.payload.EnumDTO;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.repository.CounterPartyRepository;
import com.wannabe.FinanceTracker.repository.UserRepository;
import com.wannabe.FinanceTracker.security.UserPrincipal;
import com.wannabe.FinanceTracker.utils.RoleAuthorizationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CounterPartyService {
    // TODO: Complete Service Functions

    @Autowired
    private RoleAuthorizationUtils roleAuthorizationUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CounterPartyRepository counterPartyRepository;

    @Transactional
    public GenericResponseObject<?> add(UserPrincipal userPrincipal, CounterParty addCounterPartyPayload) throws Exception {
        if (userRepository.existsByPhoneAndPhoneVerified(addCounterPartyPayload.getPhone(), true)) {
            addCounterPartyPayload.setAssociatedUserId(userRepository.findByPhone(addCounterPartyPayload.getPhone()).orElseThrow(() -> new ResourceNotFoundException("User not found")).getId());
        } else if (userRepository.existsByEmail(addCounterPartyPayload.getEmail())) {
            addCounterPartyPayload.setAssociatedUserId(userRepository.findByEmail(addCounterPartyPayload.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found")).getId());
        }

        if (!roleAuthorizationUtils.isUserAuthorized(userPrincipal, "ROLE_ADMIN")) {
            addCounterPartyPayload.setUserId(userPrincipal.getId());
        }

        addCounterPartyPayload.setId(null);

        if (!counterPartyRepository.existsByName(addCounterPartyPayload.getName())) {
            throw new ParameterValidationFailedException("Counter Party with given name already exists", ErrorCode.ALREADY_EXISTS);
        }

        try {
            counterPartyRepository.save(addCounterPartyPayload);
            return new GenericResponseObject<>(true, "Counter Party added successfully");
        } catch (Exception e) {
            log.error("Exception occurred while adding CounterParty", e);
            throw new Exception("Failed to add counter party", e);
        }
    }

    @Transactional
    public GenericResponseObject<?> update(UserPrincipal userPrincipal, CounterParty updateCounterPartyPayload) throws Exception {
        CounterParty existingCounterParty = counterPartyRepository.findById(updateCounterPartyPayload.getId()).orElseThrow(() -> new ResourceNotFoundException("Counter Party not found"));
        if (existingCounterParty.getUserId().compareTo(userPrincipal.getId()) != 0 && !roleAuthorizationUtils.isUserAuthorized(userPrincipal, "ROLE_ADMIN")) {
            throw new ResourceNotFoundException("Counter Party not found");
        }

        existingCounterParty.setName(updateCounterPartyPayload.getName());
        existingCounterParty.setPhone(updateCounterPartyPayload.getPhone());
        existingCounterParty.setEmail(updateCounterPartyPayload.getEmail());
        existingCounterParty.setType(updateCounterPartyPayload.getType());

        try {
            counterPartyRepository.save(existingCounterParty);
        } catch (Exception e) {
            log.error("Exception occurred while updating CounterParty", e);
            throw new Exception("Failed to update counter party", e);
        }

        return new GenericResponseObject<>(true, "Counter Party updated successfully");
    }

    @Transactional
    public GenericResponseObject<?> delete(UserPrincipal userPrincipal, UUID counterPartyId) throws Exception {
        return new GenericResponseObject<>(false, "Method not implemented", ErrorCode.METHOD_NOT_IMPLEMENTED);
    }

    public GenericResponseObject<?> fetchAll(UserPrincipal userPrincipal) throws Exception {
        return new GenericResponseObject<>(true, "Returning all counter parties", counterPartyRepository.findByUserId(userPrincipal.getId()));
    }

    public GenericResponseObject<?> fetch(UserPrincipal userPrincipal, UUID id) throws Exception {
        try {
            if (roleAuthorizationUtils.isUserAuthorized(userPrincipal, "ROLE_ADMIN")) {
                return new GenericResponseObject<>(true, "Returning counter party", counterPartyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Counter Party not found")));
            } else {
                return new GenericResponseObject<>(true, "Returning counter party", counterPartyRepository.findByUserIdAndId(userPrincipal.getId(), id).orElseThrow(() -> new ResourceNotFoundException("Counter Party not found")));
            }
        } catch (Exception e) {
            log.error("Exception occurred while fetching counter party", e);
            throw new Exception("Failed to fetch counter party", e);
        }
    }

    public GenericResponseObject<?> fetchAllTypes() {
        List<EnumDTO> types = new ArrayList<>();

        Arrays.stream(CounterPartyType.values()).iterator().forEachRemaining((type) -> {
            String code = type.toString();
            String name = StringUtils.capitalize(code);

            types.add(new EnumDTO(code, name));
        });

        return new GenericResponseObject<>(true, "Returning all counter party types", types);
    }
}
