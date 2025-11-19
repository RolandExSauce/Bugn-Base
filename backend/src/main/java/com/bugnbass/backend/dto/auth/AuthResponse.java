package com.bugnbass.backend.dto.auth;

public record AuthResponse(
    UserDTO user,
    String accessToken
) { }
