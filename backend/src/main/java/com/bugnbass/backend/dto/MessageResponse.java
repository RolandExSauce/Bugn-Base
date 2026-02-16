package com.bugnbass.backend.dto;

import com.bugnbass.backend.model.enums.MessageStatus;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object representing a message with optional administrative reply data.
 *
 * <p>This response is used for both sent messages and inbox views,
 * including metadata such as timestamps, status, and read state.
 */
public record MessageResponse(
        UUID id,
        String name,
        String email,
        String subject,
        String message,
        LocalDateTime createdAt,
        String adminReply,
        LocalDateTime repliedAt,
        MessageStatus messageStatus,
        LocalDateTime readAt
) {}
