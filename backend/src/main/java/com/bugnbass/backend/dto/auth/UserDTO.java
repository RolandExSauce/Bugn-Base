package com.bugnbass.backend.dto.auth;
import java.time.Instant;

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
    String role
) { }
