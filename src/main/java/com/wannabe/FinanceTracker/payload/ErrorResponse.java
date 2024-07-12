package com.wannabe.FinanceTracker.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private String type;
    private String method;
    private LocalDateTime timestamp = LocalDateTime.now();
}
