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
                order.getUser().getEmail(),
                order.getUser().getPhoneNumber(),
                "",
                order.getShippingAddress(),
                order.getTotalOrderPrice().doubleValue(),
                order.getOrderItems().stream()
                        .map(this::toItemDTO)
                        .toList(),
            order.getOrderStatus().toString()
        );
    }

    private OrderItemDTO toItemDTO(OrderItem item) {
        return new OrderItemDTO(
                String.valueOf(item.getProduct().getId()),
                item.getQuantity(),
                item.getPrice().doubleValue()
        );
    }
}