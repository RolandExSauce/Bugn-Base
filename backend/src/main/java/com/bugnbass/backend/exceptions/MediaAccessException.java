package com.bugnbass.backend.exceptions;

/**
 * Exception thrown when an error occurs while accessing,
 * reading, writing, or processing media resources.
 *
 * <p>This exception is typically used to wrap lower-level
 * I/O or storage-related exceptions and provide a
 * meaningful, domain-specific error message to the
 * application layer.
 * </p>
 *
 * <p>It extends {@link RuntimeException}, meaning it is
 * an unchecked exception and does not need to be
 * explicitly declared in method signatures.
 * </p>
 */
public class MediaAccessException extends RuntimeException {

    /**
     * Constructs a new {@code MediaAccessException} with
     * the specified detail message and underlying cause.
     *
     * @param message a descriptive error message explaining the failure
     * @param cause the original exception that caused this error
     */
    public MediaAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
