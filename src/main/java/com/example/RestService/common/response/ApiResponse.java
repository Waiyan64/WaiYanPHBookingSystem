package com.example.RestService.common.response;

import java.time.Instant;
import java.util.Map;

public record ApiResponse(Instant timestamp, 
        int code, 
        String message,
        Map<String, ?> data) {
    
}
