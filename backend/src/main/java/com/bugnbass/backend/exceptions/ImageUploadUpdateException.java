package com.bugnbass.backend.exceptions;

public class ImageUploadUpdateException extends RuntimeException {
  public ImageUploadUpdateException(String message) {
    super(message);
  }

  public ImageUploadUpdateException() {
    super("Image Upload/Update Error");
  }
}
