package com.example.RestService.common.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class UnauthorizedException extends AbstractThrowableProblem {
    public UnauthorizedException(String title, String detail) { 
        super( null, title, Status.UNAUTHORIZED, detail);
    }
}
