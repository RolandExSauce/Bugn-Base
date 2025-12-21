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
 * REST controller for user-specific order operations.
 * Provides endpoints for creating orders, retrieving individual orders,
 * listing all orders of the authenticated user, and updating order status
 * such as canceling or returning an order.
 *
 * <p>All endpoints require the user to be authenticated with either ROLE_USER or ROLE_ADMIN.</p>
 */
@RestController
@RequestMapping("/bugnbass/api/user/orders")
@RequiredArgsConstructor
public class UserOrderController {

    /** Service handling order-related business logic. */
    private final OrderService orderService;

    /**
     * Creates a new order for the authenticated user.
     *
     * @param dto the order details provided by the user
     * @return ResponseEntity containing the created order's status and HTTP 201
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<OrderStatus> createOrder(@RequestBody OrderDto dto) {
        OrderStatus status = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    /**
     * Retrieves a specific order by its ID for the authenticated user.
     *
     * @param id the ID of the order to retrieve
     * @return ResponseEntity containing the order details and HTTP 200
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable(name = "id") Long id) {
        OrderDto dto = orderService.getOrderById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves all orders associated with the authenticated user.
     *
     * @return ResponseEntity containing a list of OrderDto objects and HTTP 200
     */
    @GetMapping("/customer")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<OrderDto>> getOrdersForCustomer() {
        List<OrderDto> orders = orderService.getOrdersByCustomer();
        return ResponseEntity.ok(orders);
    }

    /**
     * Cancels an order for the authenticated user.
     *
     * @param id the ID of the order to cancel
     * @return ResponseEntity containing the updated order status and HTTP 200
     */
    @PatchMapping("/cancel/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<OrderStatus> cancelOrder(@PathVariable(name = "id") Long id) {
        OrderStatus status = orderService.cancelOrder(id);
        return ResponseEntity.ok(status);
    }

    /**
     * Marks a delivered order as returned for the authenticated user.
     *
     * @param id the ID of the order to return
     * @return ResponseEntity containing the updated order status and HTTP 200
     */
    @PatchMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<OrderStatus> returnOrder(@PathVariable(name = "id") Long id) {
        OrderStatus status = orderService.returnOrder(id);
        return ResponseEntity.ok(status);
    }
}
