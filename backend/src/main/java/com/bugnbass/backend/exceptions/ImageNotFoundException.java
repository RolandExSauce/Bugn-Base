package com.bugnbass.backend.exceptions;

public class ImageNotFoundException extends RuntimeException {
  public ImageNotFoundException(String message) {
    super(message);
  }

  public ImageNotFoundException() {
    super("Das Foto konnte nicht gefunden werden.");
  }
}
