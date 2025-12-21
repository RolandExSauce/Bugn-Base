package com.bugnbass.backend.exceptions;

/**
 * Exception thrown when an image cannot be found.
 */
public class ImageNotFoundException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ImageNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with a default message.
     */
    public ImageNotFoundException() {
        super("Das Foto konnte nicht gefunden werden.");
    }
}
