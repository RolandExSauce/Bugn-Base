package com.bugnbass.backend.dto.auth;

/**
 * DTO for user registration requests.
 *
 * @param firstname the user's first name
 * @param lastname  the user's last name
 * @param email     the user's email
 * @param password  the user's password
 */
public record RegisterDto(String firstname, String lastname, String email, String password) {
}
