package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.OrderDto;
import com.bugnbass.backend.mappers.OrderMapper;
import com.bugnbass.backend.model.Order;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.repository.OrderRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminOrderServiceTest {

    @Mock
    OrderRepository orderRepo;

    @Mock
    OrderMapper orderMapper;

    @InjectMocks
    AdminOrderService adminOrderService;

    // -------------------- getAllOrders --------------------

    @Test
    void getAllOrders_mapsAll() {
        Order o1 = mock(Order.class);
        when(orderRepo.findAll()).thenReturn(List.of(o1));

        OrderDto d1 = mock(OrderDto.class);
        when(orderMapper.toDto(o1)).thenReturn(d1);

        List<OrderDto> result = adminOrderService.getAllOrders();

        assertThat(result).containsExactly(d1);

        verify(orderRepo).findAll();
        verify(orderMapper).toDto(o1);

        verifyNoMoreInteractions(orderRepo, orderMapper);
    }

    @Test
    void getAllOrders_returnsEmptyList_whenNoOrders() {
        when(orderRepo.findAll()).thenReturn(List.of());

        List<OrderDto> result = adminOrderService.getAllOrders();

        assertThat(result).isEmpty();

        verify(orderRepo).findAll();
        verifyNoMoreInteractions(orderRepo);

        verifyNoInteractions(orderMapper);
    }

    // -------------------- getOrderById --------------------

    @Test
    void getOrderById_returnsDto() {
        Order order = mock(Order.class);
        when(orderRepo.findById(5L)).thenReturn(Optional.of(order));

        OrderDto mapped = mock(OrderDto.class);
        when(orderMapper.toDto(order)).thenReturn(mapped);

        OrderDto result = adminOrderService.getOrderById(5L);

        assertThat(result).isSameAs(mapped);

        verify(orderRepo).findById(5L);
        verify(orderMapper).toDto(order);

        verifyNoMoreInteractions(orderRepo, orderMapper);
    }

    @Test
    void getOrderById_throws_whenOrderNotFound() {
        when(orderRepo.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminOrderService.getOrderById(5L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Order not found");

        verify(orderRepo).findById(5L);
        verifyNoMoreInteractions(orderRepo);

        verifyNoInteractions(orderMapper);
    }

    // -------------------- updateOrder --------------------

    @Test
    void updateOrder_updatesStatus_andReturnsMappedDto() {
        Order order = mock(Order.class);
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        OrderDto input = new OrderDto(
                1L, null, null, List.of(), null, null,
                OrderStatus.SHIPPING, null, null, null, null
        );

        OrderDto mapped = mock(OrderDto.class);
        when(orderMapper.toDto(order)).thenReturn(mapped);

        OrderDto result = adminOrderService.updateOrder(input);

        assertThat(result).isSameAs(mapped);

        verify(orderRepo).findById(1L);
        verify(order).setOrderStatus(OrderStatus.SHIPPING);
        verify(orderRepo).save(order);
        verify(orderMapper).toDto(order);

        verifyNoMoreInteractions(orderRepo, orderMapper);
    }

    @Test
    void updateOrder_throws_whenOrderNotFound() {
        when(orderRepo.findById(1L)).thenReturn(Optional.empty());

        OrderDto input = new OrderDto(
                1L, null, null, List.of(), null, null,
                OrderStatus.SHIPPING, null, null, null, null
        );

        assertThatThrownBy(() -> adminOrderService.updateOrder(input))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Order not found");

        verify(orderRepo).findById(1L);
        verifyNoMoreInteractions(orderRepo);

        verifyNoInteractions(orderMapper);
    }

    // -------------------- deleteOrder --------------------

    @Test
    void deleteOrder_deletesWhenFound() {
        Order order = mock(Order.class);
        when(orderRepo.findById(2L)).thenReturn(Optional.of(order));

        adminOrderService.deleteOrder(2L);

        verify(orderRepo).findById(2L);
        verify(orderRepo).delete(order);

        verifyNoMoreInteractions(orderRepo);
        verifyNoInteractions(orderMapper);
    }

    @Test
    void deleteOrder_throws_whenOrderNotFound() {
        when(orderRepo.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminOrderService.deleteOrder(2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Order not found");

        verify(orderRepo).findById(2L);
        verifyNoMoreInteractions(orderRepo);

        verifyNoInteractions(orderMapper);
    }
}
