package com.bugnbass.backend.dto;
import java.util.List;

public record OrderDTO(
        String customerEmail,
        String customerPhoneNumber,
        String shippingStateAndDistrict,
        String shippingAddress,
        Double totalOrderPrice,
        List<OrderItemDTO> orderItems
) {
    public OrderDTO {
        if (customerEmail == null || customerEmail.isBlank()) {
            throw new IllegalArgumentException("Customer email is required.");
        }
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }
    }
}

