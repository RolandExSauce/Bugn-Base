package com.bugnbass.backend.dto.auth;

/**
 * DTO representing login credentials for authentication.
 *
 * @param email the user's email
 * @param password the user's password
 */
public record LoginDTO(String email, String password) {}
