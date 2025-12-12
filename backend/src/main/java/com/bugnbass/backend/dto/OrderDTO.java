package com.bugnbass.backend.dto;

import java.util.List;

/**
 * DTO representing an order. Includes customer information, shipping details, total price, and
 * order items.
 *
 * @param customerEmail the email of the customer placing the order, required
 * @param customerPhoneNumber the customer's phone number
 * @param shippingStateAndDistrict the state and district for shipping
 * @param shippingAddress the full shipping address
 * @param totalOrderPrice the total price of the order
 * @param orderItems the list of items in the order, must contain at least one item
 * @throws IllegalArgumentException if customerEmail is null/blank or orderItems is null/empty
 */
public record OrderDTO(
    String customerEmail,
    String customerPhoneNumber,
    String shippingStateAndDistrict,
    String shippingAddress,
    Double totalOrderPrice,
    List<OrderItemDTO> orderItems) {

  public OrderDTO {
    if (customerEmail == null || customerEmail.isBlank()) {
      throw new IllegalArgumentException("Customer email is required.");
    }
    if (orderItems == null || orderItems.isEmpty()) {
      throw new IllegalArgumentException("Order must contain at least one item.");
    }
  }
}
