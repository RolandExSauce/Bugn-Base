package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.OrderDto;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST controller for user-related order operations.
 * Allows users to create orders, view their own orders, cancel, and return orders.
 */
@RestController
@RequestMapping("/user/orders")
@RequiredArgsConstructor
public class UserOrderController {

    /** Service handling order-related business logic. */
    private final OrderService orderService;

    /**
     * Creates a new order for the authenticated user.
     *
     * @param dto the order details
     * @return HTTP 201 Created with the status of the new order
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<OrderStatus> createOrder(@RequestBody OrderDto dto) {
        OrderStatus status = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the order ID
     * @return the order details if accessible by the user
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        OrderDto dto = orderService.getOrderById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves all orders for the authenticated user.
     *
     * @return list of the user's orders
     */
    @GetMapping("/customer")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<OrderDto>> getOrdersForCustomer() {
        List<OrderDto> orders = orderService.getOrdersByCustomer();
        return ResponseEntity.ok(orders);
    }

    /**
     * Cancels an order belonging to the authenticated user.
     *
     * @param id the order ID
     * @return the new status of the order
     */
    @PatchMapping("/cancel/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<OrderStatus> cancelOrder(@PathVariable Long id) {
        OrderStatus status = orderService.cancelOrder(id);
        return ResponseEntity.ok(status);
    }

    /**
     * Marks a delivered order as returned for the authenticated user.
     *
     * @param id the order ID
     * @return the new status of the order
     */
    @PatchMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<OrderStatus> returnOrder(@PathVariable Long id) {
        OrderStatus status = orderService.returnOrder(id);
        return ResponseEntity.ok(status);
    }
}
