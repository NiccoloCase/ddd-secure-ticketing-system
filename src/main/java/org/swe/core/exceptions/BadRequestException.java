package org.swe.core.exceptions;

public class BadRequestException extends ApplicationException {
    public BadRequestException(String message) {
        super(message, 400);
    }
}
