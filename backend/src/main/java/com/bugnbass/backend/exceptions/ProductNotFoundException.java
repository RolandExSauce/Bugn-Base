package com.bugnbass.backend.exceptions;

/**
 * Exception thrown when a requested product cannot be found in the database.
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Constructs a new exception with a default message.
     */
    public ProductNotFoundException() {
        super("Produkt konnte nicht gefunden werden.");
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ProductNotFoundException(String message) {
        super(message);
    }
}
