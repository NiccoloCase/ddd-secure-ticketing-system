package org.swe.core.exceptions;

public class UnauthorizedException extends ApplicationException {
    public UnauthorizedException(String message) {
        super(message, 401);
    }
}