package com.bugnbass.backend.dto;

import com.bugnbass.backend.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
