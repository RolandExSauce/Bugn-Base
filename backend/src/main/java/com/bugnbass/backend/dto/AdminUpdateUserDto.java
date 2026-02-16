package com.bugnbass.backend.dto;

import com.bugnbass.backend.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object used by administrators to update user information.
 *
 * <p>This DTO contains both profile data and administrative properties
 * such as account status and role.
 *
 * <p>Validation constraints ensure required fields are present and properly formatted.
 */
public record AdminUpdateUserDto(

        @NotBlank
        String firstname,

        @NotBlank
        String lastname,

        Integer phone,

        String address,

        @NotNull
        String postcode,

        @Email
        @NotBlank
        String email,

        @NotNull
        Boolean active,

        @NotNull
        UserRole role

) {}
