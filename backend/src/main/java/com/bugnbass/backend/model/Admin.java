package com.bugnbass.backend.model;
import com.bugnbass.backend.model.enums.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

public class Admin {

    @Id
    private Long id;

    String name;
    String email;

    @Enumerated(EnumType.STRING)
    UserRole role = UserRole.ROLE_ADMIN;
};


