package com.bugnbass.backend.dto;

import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.model.enums.PaymentMethod;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO representing an order.
 *
 * @param id               the unique identifier of the order
 * @param orderNumber      the order number
 * @param totalOrderPrice  the total price of the order
 * @param orderItems       the list of items in the order
 * @param orderedDate      the date when the order was placed
 * @param deliveryDate     the expected delivery date
 * @param orderStatus      the current status of the order
 * @param shippingAddress  the shipping address for the order
 * @param paymentMethod    the payment method used for the order
 * @param deliveryFullname the recipient's full name for delivery
 * @param deliveryPostcode the recipient's postcode for delivery
 */
public record OrderDto(
    Long id,
    String orderNumber,
    Integer totalOrderPrice,
    List<OrderItemDto> orderItems,
    LocalDate orderedDate,
    LocalDate deliveryDate,
    OrderStatus orderStatus,
    String shippingAddress,
    PaymentMethod paymentMethod,
    String deliveryFullname,
    Integer deliveryPostcode
) {
}
