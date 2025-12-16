package com.bugnbass.backend.dto;

public record OrderItemDTO(
        String productId,
        int quantity,
        double price
) {
    public OrderItemDTO {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Product ID is required");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    };

    //get productId as Long (for SQL)
    public Long getProductIdAsLong() {
        try {
            return Long.parseLong(productId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Product ID must be numeric for SQL databases");
        }
    };

    public double getItemTotal() {
        return quantity * price;
    };
};