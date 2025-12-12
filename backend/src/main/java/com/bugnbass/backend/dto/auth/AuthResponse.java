package com.bugnbass.backend.dto.auth;

/**
 * DTO representing the authentication response. Includes user details and the generated access
 * token.
 *
 * @param user the authenticated user's information
 * @param accessToken the JWT access token
 */
public record AuthResponse(UserDTO user, String accessToken) {}
