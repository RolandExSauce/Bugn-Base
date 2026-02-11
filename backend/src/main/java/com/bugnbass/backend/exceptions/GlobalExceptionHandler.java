package com.bugnbass.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global exception handler for the application.
 *
 * <p>Maps application- and security-related exceptions to appropriate HTTP
 * status codes so that the frontend can distinguish between
 * authentication errors (401), authorization errors (403),
 * and internal server errors (500).
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles cases where a requested product cannot be found.
   *
   * @param e the thrown {@link ProductNotFoundException}
   * @return a 404 NOT FOUND response with the exception message
   */
  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<String> handleProductNotFoundExc(ProductNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  /**
   * Handles attempts to create or update a product with a name
   * that already exists.
   *
   * @param e the thrown {@link ProductNameAlreadyExistsException}
   * @return a 404 NOT FOUND response with the exception message
   */
  @ExceptionHandler(ProductNameAlreadyExistsException.class)
  public ResponseEntity<String> handleProductNameAlreadyExistsExc(
          ProductNameAlreadyExistsException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  /**
   * Handles cases where an image resource cannot be found.
   *
   * @param e the thrown {@link ImageNotFoundException}
   * @return a 404 NOT FOUND response with the exception message
   */
  @ExceptionHandler(ImageNotFoundException.class)
  public ResponseEntity<String> handleImageNotFoundExc(ImageNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  /**
   * Handles errors occurring during image upload or update operations.
   *
   * @param e the thrown {@link ImageUploadUpdateException}
   * @return a 500 INTERNAL SERVER ERROR response with the exception message
   */
  @ExceptionHandler(ImageUploadUpdateException.class)
  public ResponseEntity<String> handleImageUploadUpdateExc(ImageUploadUpdateException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }

  /**
   * Handles authorization failures caused by method-level security
   * (e.g. {@code @PreAuthorize}).
   *
   * <p>This exception indicates that the user is authenticated
   * but does not have sufficient permissions.
   * </p>
   *
   * @param e the thrown {@link AuthorizationDeniedException}
   * @return a 403 FORBIDDEN response
   */
  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<String> handleAuthorizationDenied(AuthorizationDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
  }

  /**
   * Handles access denial triggered by URL-based or filter-based
   * Spring Security authorization checks.
   *
   * @param e the thrown {@link AccessDeniedException}
   * @return a 403 FORBIDDEN response
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDenied(AccessDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
  }

  /**
   * Handles authentication-related errors such as invalid or missing credentials.
   *
   * @param e the thrown {@link AuthenticationException}
   * @return a 401 UNAUTHORIZED response
   */
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
  }

  /**
   * Handles all uncaught exceptions that are not explicitly mapped
   * to a specific HTTP status.
   *
   * @param e the thrown {@link Exception}
   * @return a 500 INTERNAL SERVER ERROR response with the exception message
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericExc(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Invalid request parameter");
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleNotReadable(HttpMessageNotReadableException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Invalid request body");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidation(MethodArgumentNotValidException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Validation failed");
  }


}
