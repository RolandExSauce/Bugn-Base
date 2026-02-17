package com.bugnbass.backend.dto;

import java.time.LocalDateTime;

/**
 * Response DTO representing a product review.
 * Contains review metadata including the author, rating, comment, and creation timestamp.
 */
public record ReviewResponseDto(
        String id,
        String userName,
        Integer rating,
        String comment,
        LocalDateTime createdAt
) {}
