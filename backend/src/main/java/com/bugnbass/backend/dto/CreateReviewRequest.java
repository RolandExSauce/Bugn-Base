package com.bugnbass.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating a new product review.
 *
 * <p>Contains the product identifier, a rating between 1 and 5, and an optional comment.
 */
public record CreateReviewRequest(

        @NotNull
        Long productId,

        @Min(1)
        @Max(5)
        Integer rating,

        String comment
) {}
