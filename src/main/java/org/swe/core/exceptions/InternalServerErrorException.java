package org.swe.core.exceptions;

public class InternalServerErrorException extends ApplicationException {
    public InternalServerErrorException(String message) {
        super(message, 500);
    }
}