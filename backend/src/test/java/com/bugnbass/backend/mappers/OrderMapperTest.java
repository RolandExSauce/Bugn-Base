package com.bugnbass.backend.mappers;

import com.bugnbass.backend.dto.OrderDto;
import com.bugnbass.backend.dto.OrderItemDto;
import com.bugnbass.backend.model.Order;
import com.bugnbass.backend.model.OrderItem;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.model.enums.PaymentMethod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderMapperTest {

    private final OrderMapper orderMapper = new OrderMapper();

    @Test
    void toDto_mapsAllFieldsCorrectly_withSingleItem() {
        // Arrange
        LocalDate ordered = LocalDate.of(2026, 2, 10);
        LocalDate delivery = LocalDate.of(2026, 2, 24);

        Product product = mock(Product.class);
        when(product.getId()).thenReturn(99L);

        OrderItem item = mock(OrderItem.class);
        when(item.getProduct()).thenReturn(product);
        when(item.getQuantity()).thenReturn(2);
        when(item.getPrice()).thenReturn(500);

        Order order = mock(Order.class);
        when(order.getId()).thenReturn(7L);
        when(order.getOrderNumber()).thenReturn("ORD-123");
        when(order.getTotalOrderPrice()).thenReturn(1000);
        when(order.getOrderItems()).thenReturn(List.of(item));
        when(order.getOrderedDate()).thenReturn(ordered);
        when(order.getDeliveryDate()).thenReturn(delivery);
        when(order.getOrderStatus()).thenReturn(OrderStatus.RECEIVED);
        when(order.getShippingAddress()).thenReturn("Street 1");
        when(order.getPaymentMethod()).thenReturn(PaymentMethod.class.getEnumConstants()[0]);
        when(order.getDeliveryFullname()).thenReturn("Max Mustermann");
        when(order.getDeliveryPostcode()).thenReturn(1010);

        // Act
        OrderDto dto = orderMapper.toDto(order);

        // Assert (Order-Felder)
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(7L);
        assertThat(dto.orderNumber()).isEqualTo("ORD-123");
        assertThat(dto.totalOrderPrice()).isEqualTo(1000);
        assertThat(dto.orderedDate()).isEqualTo(ordered);
        assertThat(dto.deliveryDate()).isEqualTo(delivery);
        assertThat(dto.orderStatus()).isEqualTo(OrderStatus.RECEIVED);
        assertThat(dto.shippingAddress()).isEqualTo("Street 1");
        assertThat(dto.paymentMethod()).isNotNull();
        assertThat(dto.deliveryFullname()).isEqualTo("Max Mustermann");
        assertThat(dto.deliveryPostcode()).isEqualTo(1010);

        // Assert (Items)
        assertThat(dto.orderItems()).hasSize(1);
        OrderItemDto itemDto = dto.orderItems().get(0);
        assertThat(itemDto.productId()).isEqualTo(99L);
        assertThat(itemDto.quantity()).isEqualTo(2);
        assertThat(itemDto.price()).isEqualTo(500);
    }

    @Test
    void toDto_mapsEmptyItemsList_toEmptyDtoItems() {
        // Arrange
        Order order = mock(Order.class);
        when(order.getId()).thenReturn(1L);
        when(order.getOrderNumber()).thenReturn("ORD-EMPTY");
        when(order.getTotalOrderPrice()).thenReturn(0);
        when(order.getOrderItems()).thenReturn(List.of());
        when(order.getOrderedDate()).thenReturn(LocalDate.of(2026, 2, 10));
        when(order.getDeliveryDate()).thenReturn(LocalDate.of(2026, 2, 24));
        when(order.getOrderStatus()).thenReturn(OrderStatus.RECEIVED);
        when(order.getShippingAddress()).thenReturn("Somewhere");
        when(order.getPaymentMethod()).thenReturn(PaymentMethod.class.getEnumConstants()[0]);
        when(order.getDeliveryFullname()).thenReturn("Nobody");
        when(order.getDeliveryPostcode()).thenReturn(9999);

        // Act
        OrderDto dto = orderMapper.toDto(order);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.orderItems()).isNotNull();
        assertThat(dto.orderItems()).isEmpty();
    }

    @Test
    void toDto_mapsMultipleItems_andPreservesOrder() {
        // Arrange
        Product p1 = mock(Product.class);
        when(p1.getId()).thenReturn(1L);
        OrderItem i1 = mock(OrderItem.class);
        when(i1.getProduct()).thenReturn(p1);
        when(i1.getQuantity()).thenReturn(1);
        when(i1.getPrice()).thenReturn(100);

        Product p2 = mock(Product.class);
        when(p2.getId()).thenReturn(2L);
        OrderItem i2 = mock(OrderItem.class);
        when(i2.getProduct()).thenReturn(p2);
        when(i2.getQuantity()).thenReturn(3);
        when(i2.getPrice()).thenReturn(200);

        Order order = mock(Order.class);
        when(order.getId()).thenReturn(42L);
        when(order.getOrderNumber()).thenReturn("ORD-MULTI");
        when(order.getTotalOrderPrice()).thenReturn(300);
        when(order.getOrderItems()).thenReturn(List.of(i1, i2));
        when(order.getOrderedDate()).thenReturn(LocalDate.of(2026, 2, 10));
        when(order.getDeliveryDate()).thenReturn(LocalDate.of(2026, 2, 24));
        when(order.getOrderStatus()).thenReturn(OrderStatus.RECEIVED);
        when(order.getShippingAddress()).thenReturn("Street 2");
        when(order.getPaymentMethod()).thenReturn(PaymentMethod.class.getEnumConstants()[0]);
        when(order.getDeliveryFullname()).thenReturn("Max Mustermann");
        when(order.getDeliveryPostcode()).thenReturn(1010);

        // Act
        OrderDto dto = orderMapper.toDto(order);

        // Assert
        assertThat(dto.orderItems()).hasSize(2);
        assertThat(dto.orderItems().get(0).productId()).isEqualTo(1L);
        assertThat(dto.orderItems().get(0).quantity()).isEqualTo(1);
        assertThat(dto.orderItems().get(0).price()).isEqualTo(100);

        assertThat(dto.orderItems().get(1).productId()).isEqualTo(2L);
        assertThat(dto.orderItems().get(1).quantity()).isEqualTo(3);
        assertThat(dto.orderItems().get(1).price()).isEqualTo(200);
    }
}
