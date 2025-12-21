package com.bugnbass.backend.dto.auth;

/**
 * DTO representing the authentication response returned after login or registration.
 *
 * @param user        the authenticated user's details
 * @param accessToken the JWT access token for authenticated requests
 */
public record AuthResponse(
    UserDto user,
    String accessToken
) {
}
