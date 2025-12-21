package com.bugnbass.backend.dto.auth;

/**
 * DTO for login requests.
 *
 * @param email    the user's email
 * @param password the user's password
 */
public record LoginDto(String email, String password) {
}
