package com.bugnbass.backend.exceptions;

public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException() {
    super("Produkt konnte nicht gefunden werden.");
  }

  public ProductNotFoundException(String message) {
    super(message);
  }
}
;
