package com.bugnbass.backend.dto.auth;
import java.time.Instant;

public record UserDTO(
    String id,
    String firstname,
    String lastname,
    String phone,
    String address,
    Integer postcode,
    String email,
    boolean active,
    Instant createdAt,
    String role
) { }
