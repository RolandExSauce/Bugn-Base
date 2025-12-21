package com.bugnbass.backend.exceptions;

/**
 * Exception thrown when a requested user cannot be found in the system.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
