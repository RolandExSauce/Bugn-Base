package com.bugnbass.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the application.
 * Catches specific exceptions and returns appropriate HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles ProductNotFoundException.
   *
   * @param e the exception
   * @return 404 NOT FOUND with the exception message
   */
  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<String> handleProductNotFoundExc(ProductNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  /**
   * Handles ProductNameAlreadyExistsException.
   *
   * @param e the exception
   * @return 404 NOT FOUND with the exception message
   */
  @ExceptionHandler(ProductNameAlreadyExistsException.class)
  public ResponseEntity<String> handleProductNameAlreadyExistsExc(
          ProductNameAlreadyExistsException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  /**
   * Handles ImageNotFoundException.
   *
   * @param e the exception
   * @return 404 NOT FOUND with the exception message
   */
  @ExceptionHandler(ImageNotFoundException.class)
  public ResponseEntity<String> handleImageNotFoundExc(ImageNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  /**
   * Handles ImageUploadUpdateException.
   *
   * @param e the exception
   * @return 500 INTERNAL SERVER ERROR with the exception message
   */
  @ExceptionHandler(ImageUploadUpdateException.class)
  public ResponseEntity<String> handleImageUploadUpdateExc(ImageUploadUpdateException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }

  /**
   * Handles generic exceptions.
   *
   * @param e the exception
   * @return 500 INTERNAL SERVER ERROR with the exception message
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericExc(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }
}
