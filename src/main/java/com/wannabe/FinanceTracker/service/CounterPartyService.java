package com.wannabe.FinanceTracker.service;

import com.wannabe.FinanceTracker.exception.ResourceNotFoundException;
import com.wannabe.FinanceTracker.model.CounterParty;
import com.wannabe.FinanceTracker.model.CounterPartyType;
import com.wannabe.FinanceTracker.model.ErrorCode;
import com.wannabe.FinanceTracker.payload.EnumDTO;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import com.wannabe.FinanceTracker.repository.UserRepository;
import com.wannabe.FinanceTracker.security.UserPrincipal;
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
    private UserRepository userRepository;

    @Transactional
    public GenericResponseObject<?> add(UserPrincipal userPrincipal, CounterParty addCounterPartyPayload) throws Exception {
        if (userRepository.existsByPhone(addCounterPartyPayload.getPhone())) {
            addCounterPartyPayload.setAssociatedUserId(userRepository.findByPhone(addCounterPartyPayload.getPhone()).orElseThrow(() -> new ResourceNotFoundException("User not found")).getId());
        }
        return new GenericResponseObject<>(false, "Method not implemented", ErrorCode.METHOD_NOT_IMPLEMENTED);
    }

    @Transactional
    public GenericResponseObject<?> update(UserPrincipal userPrincipal, CounterParty updateCounterPartyPayload) throws Exception {
        if (userRepository.existsByPhone(updateCounterPartyPayload.getPhone())) {
            updateCounterPartyPayload.setAssociatedUserId(userRepository.findByPhone(updateCounterPartyPayload.getPhone()).orElseThrow(() -> new ResourceNotFoundException("User not found")).getId());
        }
        return new GenericResponseObject<>(false, "Method not implemented", ErrorCode.METHOD_NOT_IMPLEMENTED);
    }

    @Transactional
    public GenericResponseObject<?> delete(UserPrincipal userPrincipal, UUID counterPartyId) throws Exception {
        return new GenericResponseObject<>(false, "Method not implemented", ErrorCode.METHOD_NOT_IMPLEMENTED);
    }

    public GenericResponseObject<?> fetch(UserPrincipal userPrincipal) throws Exception {
        return new GenericResponseObject<>(false, "Method not implemented", ErrorCode.METHOD_NOT_IMPLEMENTED);
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
