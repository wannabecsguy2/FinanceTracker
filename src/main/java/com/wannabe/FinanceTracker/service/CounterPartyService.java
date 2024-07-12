package com.wannabe.FinanceTracker.service;

import com.wannabe.FinanceTracker.model.CounterPartyType;
import com.wannabe.FinanceTracker.payload.EnumDTO;
import com.wannabe.FinanceTracker.payload.GenericResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class CounterPartyService {
    public GenericResponseObject<?> addCounterParty() {
        return null;
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
