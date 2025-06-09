package com.bugnbass.backend.exceptions;

public class ProductNameAlreadyExistsException extends RuntimeException {
    public ProductNameAlreadyExistsException() { super("Produktname existiert bereits");}
    public ProductNameAlreadyExistsException(String message) {
        super(message);
    }
}
