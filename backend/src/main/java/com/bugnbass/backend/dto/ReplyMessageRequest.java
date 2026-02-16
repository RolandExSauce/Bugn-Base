package com.bugnbass.backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object used by administrators to send a reply to a message.
 *
 * <p>This request contains the reply text that will be stored
 * and associated with an existing message.
 */
public record ReplyMessageRequest(
        @NotBlank String reply
) {}
