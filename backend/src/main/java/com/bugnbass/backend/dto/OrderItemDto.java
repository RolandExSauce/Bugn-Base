package com.bugnbass.backend.dto;

/**
 * DTO representing a single item within an order.
 *
 * @param productId   the unique identifier of the product
 * @param productName the display name of the product
 * @param quantity    the quantity of the product ordered
 * @param price       the price of a single unit of the product
 */
public record OrderItemDto(
        Long productId,
        String productName,
        Integer quantity,
        Integer price
) {}
