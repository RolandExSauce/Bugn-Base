package com.bugnbass.backend.mappers;

import com.bugnbass.backend.dto.OrderDTO;
import com.bugnbass.backend.dto.OrderItemDTO;
import com.bugnbass.backend.model.Order;
import com.bugnbass.backend.model.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        return new OrderDTO(
            order.getId(),
            order.getOrderNumber(),
            order.getTotalOrderPrice(),
            order.getOrderItems()
                .stream()
                .map(this::toItemDTO)
                .toList(),
            order.getOrderedDate(),
            order.getDeliveryDate(),
            order.getOrderStatus(),
            order.getShippingAddress(),
            order.getPaymentMethod()
        );
    }

    private OrderItemDTO toItemDTO(OrderItem item) {
        return new OrderItemDTO(
            item.getProduct().getId(),
            item.getQuantity(),
            item.getPrice()
        );
    }
}
