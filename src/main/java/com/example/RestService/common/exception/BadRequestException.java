package com.example.RestService.common.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class BadRequestException extends AbstractThrowableProblem {
    
    public BadRequestException(String title, String detail) { 
        super( null, title,
        Status.BAD_REQUEST, detail);
    }
}
