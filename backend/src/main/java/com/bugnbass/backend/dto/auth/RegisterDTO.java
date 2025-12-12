package com.bugnbass.backend.dto.auth;

/**
 * DTO representing registration data for a new user.
 *
 * @param firstname the user's first name
 * @param lastname the user's last name
 * @param email the user's email
 * @param password the user's password
 */
public record RegisterDTO(String firstname, String lastname, String email, String password) {}
