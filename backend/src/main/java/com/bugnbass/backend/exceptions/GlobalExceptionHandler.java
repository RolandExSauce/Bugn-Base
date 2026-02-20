package com.bugnbass.backend.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
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
 * <p>This class centralizes exception handling across all controllers
 * using {@link RestControllerAdvice}. It maps application-specific,
 * validation-related, and Spring Security exceptions to appropriate
 * HTTP status codes so that the frontend can reliably distinguish
 * between different error categories (e.g. 400, 401, 403, 404, 500).
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles cases where a requested product cannot be found.
   *
   * @param e the thrown {@link ProductNotFoundException}
   * @return a {@link ResponseEntity} with HTTP 404 (NOT FOUND)
   *         containing the exception message
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
   * @return a {@link ResponseEntity} with HTTP 404 (NOT FOUND)
   *         containing the exception message
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
   * @return a {@link ResponseEntity} with HTTP 404 (NOT FOUND)
   *         containing the exception message
   */
  @ExceptionHandler(ImageNotFoundException.class)
  public ResponseEntity<String> handleImageNotFoundExc(ImageNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  /**
   * Handles errors occurring during image upload or update operations.
   *
   * @param e the thrown {@link ImageUploadUpdateException}
   * @return a {@link ResponseEntity} with HTTP 500 (INTERNAL SERVER ERROR)
   *         containing the exception message
   */
  @ExceptionHandler(ImageUploadUpdateException.class)
  public ResponseEntity<String> handleImageUploadUpdateExc(ImageUploadUpdateException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }

  /**
   * Handles authorization failures caused by method-level security
   * annotations such as {@code @PreAuthorize}.
   *
   * <p>This indicates that the user is authenticated but does not have
   * sufficient permissions to access the requested resource.
   * </p>
   *
   * @param e the thrown {@link AuthorizationDeniedException}
   * @return a {@link ResponseEntity} with HTTP 403 (FORBIDDEN)
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
   * @return a {@link ResponseEntity} with HTTP 403 (FORBIDDEN)
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDenied(AccessDeniedException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
  }

  /**
   * Handles authentication-related errors such as invalid, expired,
   * or missing credentials.
   *
   * @param e the thrown {@link AuthenticationException}
   * @return a {@link ResponseEntity} with HTTP 401 (UNAUTHORIZED)
   */
  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
  }

  /**
   * Handles method argument type mismatches, such as when a request
   * parameter cannot be converted to the required target type.
   *
   * @param e the thrown {@link MethodArgumentTypeMismatchException}
   * @return a {@link ResponseEntity} with HTTP 400 (BAD REQUEST)
   *         and a generic error message
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Invalid request parameter");
  }

  /**
   * Handles malformed or unreadable HTTP request bodies,
   * for example invalid JSON syntax.
   *
   * @param e the thrown {@link HttpMessageNotReadableException}
   * @return a {@link ResponseEntity} with HTTP 400 (BAD REQUEST)
   *         and a generic error message
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleNotReadable(HttpMessageNotReadableException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Invalid request body");
  }

  /**
   * Handles validation errors triggered by {@code @Valid}
   * annotated request bodies or parameters.
   *
   * @param e the thrown {@link MethodArgumentNotValidException}
   * @return a {@link ResponseEntity} with HTTP 400 (BAD REQUEST)
   *         and a generic validation error message
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidation(MethodArgumentNotValidException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Validation failed");
  }

  /**
   * Handles data integrity violations (like duplicate email constraints).
   *
   * @param e the thrown {@link DataIntegrityViolationException}
   * @return a {@link ResponseEntity} with HTTP 409 (CONFLICT)
   *         and a user-friendly error message
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException e) {
    // Check if it's a duplicate email violation
    if (e.getMessage() != null && e.getMessage().contains("users_email_key")) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
              .body("Diese E-Mail-Adresse wird bereits verwendet");
    }

    // Generic database error for other constraints
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Ein Datenbankfehler ist aufgetreten");
  }

  /**
   * Handles media access related errors, such as failures when
   * accessing or processing stored media resources.
   *
   * @param e the thrown {@link MediaAccessException}
   * @return a {@link ResponseEntity} with HTTP 500 (INTERNAL SERVER ERROR)
   *         and a generic error message
   */
  @ExceptionHandler(MediaAccessException.class)
  public ResponseEntity<String> handleMediaAccess(MediaAccessException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Media access error");
  }

  /**
   * Handles all uncaught exceptions that are not explicitly mapped
   * to a specific HTTP status.
   *
   * <p>This serves as a fallback handler to prevent unhandled exceptions
   * from leaking internal details to the client.
   * </p>
   *
   * @param e the thrown {@link Exception}
   * @return a {@link ResponseEntity} with HTTP 500 (INTERNAL SERVER ERROR)
   *         containing the exception message
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericExc(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }
}
