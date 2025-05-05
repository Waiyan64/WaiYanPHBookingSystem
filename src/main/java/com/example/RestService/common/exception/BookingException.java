package com.example.RestService.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookingException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public BookingException(String message) {
        super(message);
    }

    
}
