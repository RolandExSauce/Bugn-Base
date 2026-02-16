package com.bugnbass.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object used to create a new message.
 *
 * <p>This request typically originates from a contact form or
 * user messaging interface and contains the sender's contact
 * information and message content.
 *
 * <p>Validation constraints ensure required fields are provided
 * and correctly formatted.
 */
public record MessageRequest(

        @NotBlank
        String name,

        @NotBlank
        @Email
        String email,

        String subject,

        @NotBlank
        String message
) {}
