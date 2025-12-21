package com.bugnbass.backend.exceptions;

/**
 * Exception thrown when attempting to create or update a product with a name that already exists.
 */
public class ProductNameAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new exception with a default message.
     */
    public ProductNameAlreadyExistsException() {
        super("Produktname existiert bereits");
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ProductNameAlreadyExistsException(String message) {
        super(message);
    }
}
