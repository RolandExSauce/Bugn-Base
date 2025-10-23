package com.bugnbass.backend.dto;
import java.util.List;

public record OrderDTO(
        String userEmailToMakeOrderFor,
        String updatedOrNotUserEmail,
        String updatedOrNotUserPhoneNumber,
        String updatedOrNotStateAndDistrict,
        String updatedOrNotShippingAddress,
        Double totalOrderPrice,
        List<OrderItemDTO> orderItems
) {

    public OrderDTO {
        if (userEmailToMakeOrderFor == null || userEmailToMakeOrderFor.isBlank()) {
            throw new IllegalArgumentException("user email is required");
        }
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
    };

    public String getUserEmail() {
        return updatedOrNotUserEmail != null ? updatedOrNotUserEmail : userEmailToMakeOrderFor;
    }

    public boolean isCustomerInfoUpdated() {
        return updatedOrNotUserEmail != null ||
                updatedOrNotUserPhoneNumber != null ||
                updatedOrNotStateAndDistrict != null ||
                updatedOrNotShippingAddress != null;
    }
}
