package com.bugnbass.backend.dto;

public record OrderItemDTO(
        Long productId,
        Integer quantity,
        Integer price
) {}