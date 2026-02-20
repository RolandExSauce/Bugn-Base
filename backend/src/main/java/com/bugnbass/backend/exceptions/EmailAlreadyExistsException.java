package com.bugnbass.backend.exceptions;

/**
 * Exception thrown when trying to register with an email that already exists.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Creates a new EmailAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}