package com.example.RestService.common.response;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class HttpResponse {
    
    public static ResponseEntity<?> build(ApiResponse body, HttpStatus httpStatus) { 
        return new ResponseEntity<>(body, httpStatus);
    }

    public static ResponseEntity<?> success(String message, Map<String, ?> data) { 
        return build(
            new ApiResponse(
                Instant.now(), 
                HttpStatus.OK.value(), 
                message, 
                data), 
                HttpStatus.OK);
    }

    public static ResponseEntity<?> created(String message, Map<String, ?> data) { 
        return build(
            new ApiResponse(
                Instant.now(), 
                HttpStatus.CREATED.value(),
                message, 
                data),  
                HttpStatus.CREATED);
    }
}
