package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.OrderDto;
import com.bugnbass.backend.mappers.OrderMapper;
import com.bugnbass.backend.model.Order;
import com.bugnbass.backend.repository.OrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for administrative order operations.
 *
 * <p>Allows administrators to view, update, and delete orders.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdminOrderService {

    private final OrderRepository orderRepo;
    private final OrderMapper orderMapper;

    /**
     * Retrieves all orders in the system.
     */
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepo.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    /**
     * Retrieves an order by its identifier.
     */
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return orderMapper.toDto(order);
    }

    /**
     * Updates an order status.
     */
    public OrderDto updateOrder(OrderDto orderDto) {
        Order order = orderRepo.findById(orderDto.id())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(orderDto.orderStatus());
        orderRepo.save(order);

        return orderMapper.toDto(order);
    }

    /**
     * Deletes an order.
     */
    public void deleteOrder(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        orderRepo.delete(order);
    }
}
