package com.example.RestService.common.exception;

public class InsufficientPointsException extends RuntimeException{
    public InsufficientPointsException(String message) { 
        super(message);
    }
}
