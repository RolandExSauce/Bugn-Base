package com.bugnbass.backend.dto.auth;

import java.time.Instant;

/**
 * DTO representing a user or admin for authentication responses.
 *
 * @param id the unique identifier of the user
 * @param firstname the user's first name
 * @param lastname the user's last name
 * @param phone the user's phone number
 * @param address the user's address
 * @param postcode the user's postal code
 * @param email the user's email
 * @param active whether the user is active
 * @param createdAt the timestamp when the user was created
 * @param role the role of the user (e.g., ROLE_USER, ROLE_ADMIN)
 */
public record UserDTO(
    String id,
    String firstname,
    String lastname,
    Integer phone,
    String address,
    String postcode,
    String email,
    boolean active,
    Instant createdAt,
    String role) {}
