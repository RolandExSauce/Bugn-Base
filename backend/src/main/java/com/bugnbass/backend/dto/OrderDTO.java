package com.bugnbass.backend.dto;

import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.model.enums.PaymentMethod;

import java.time.LocalDate;
import java.util.List;

public record OrderDTO(
    Long id,
    String orderNumber,
    Integer totalOrderPrice,
    List<OrderItemDTO> orderItems,
    LocalDate orderedDate,
    LocalDate deliveryDate,
    OrderStatus orderStatus,
    String shippingAddress,
    PaymentMethod paymentMethod
) {
}
