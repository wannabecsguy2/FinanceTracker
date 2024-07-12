package com.wannabe.FinanceTracker.payload;

import lombok.Data;

@Data
public class EnumDTO {
    private String code;
    private String name;

    public EnumDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
