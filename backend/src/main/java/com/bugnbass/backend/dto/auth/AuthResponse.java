package com.bugnbass.backend.dto.auth;

public record AuthResponse(String email, String username, String token) {
}
