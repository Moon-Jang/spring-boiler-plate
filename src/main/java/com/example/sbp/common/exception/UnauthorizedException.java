package com.example.sbp.common.exception;

import com.example.sbp.common.support.Status;

public class UnauthorizedException extends ApplicationException {
    public UnauthorizedException(Status status) {
        super(status);
    }

    public UnauthorizedException(Status status, String message) {
        super(status, message);
    }

    public UnauthorizedException(Status status, Throwable cause) {
        super(status, cause);
    }

    public UnauthorizedException(Status status, String message, Throwable cause) {
        super(status, message, cause);
    }
}