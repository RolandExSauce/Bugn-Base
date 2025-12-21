package com.bugnbass.backend.mappers;

import com.bugnbass.backend.dto.OrderDto;
import com.bugnbass.backend.dto.OrderItemDto;
import com.bugnbass.backend.model.Order;
import com.bugnbass.backend.model.OrderItem;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting {@link Order} entities to {@link OrderDto} objects.
 * Also maps {@link OrderItem} entities to {@link OrderItemDto}.
 */
@Component
public class OrderMapper {

  /**
   * Converts an {@link Order} entity to its corresponding {@link OrderDto}.
   *
   * @param order the order entity to convert
   * @return the mapped OrderDTO
   */
  public OrderDto toDto(Order order) {
    return new OrderDto(
        order.getId(),
        order.getOrderNumber(),
        order.getTotalOrderPrice(),
        order.getOrderItems()
            .stream()
            .map(this::toItemDto)
            .toList(),
        order.getOrderedDate(),
        order.getDeliveryDate(),
        order.getOrderStatus(),
        order.getShippingAddress(),
        order.getPaymentMethod(),
        order.getDeliveryFullname(),
        order.getDeliveryPostcode()
    );
  }

  /**
   * Converts an {@link OrderItem} entity to its corresponding {@link OrderItemDto}.
   *
   * @param item the order item entity to convert
   * @return the mapped OrderItemDTO
   */
  private OrderItemDto toItemDto(OrderItem item) {
    return new OrderItemDto(
        item.getProduct().getId(),
        item.getQuantity(),
        item.getPrice()
    );
  }
}
