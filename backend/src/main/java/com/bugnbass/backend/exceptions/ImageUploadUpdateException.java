package com.bugnbass.backend.exceptions;

/**
 * Exception thrown when an image upload or update operation fails.
 */
public class ImageUploadUpdateException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public ImageUploadUpdateException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with a default message.
     */
    public ImageUploadUpdateException() {
        super("Image Upload/Update Error");
    }
}
