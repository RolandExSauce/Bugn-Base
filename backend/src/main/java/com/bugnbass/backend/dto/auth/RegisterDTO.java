package com.bugnbass.backend.dto.auth;
import com.bugnbass.backend.model.enums.UserRole;

public record RegisterDTO(String firstName, String lastName, String email, String password) {}
