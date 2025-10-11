package com.bugnbass.backend.dto;

import com.bugnbass.backend.model.enums.UserRole;

public record RegisterDTO(String username, String email, String password, UserRole role) {
}
