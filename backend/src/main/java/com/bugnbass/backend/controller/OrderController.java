package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.OrderDTO;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Creates a new order for a customer.
     *
     * Only users with role USER can create orders.
     *
     * @param dto the order data transfer object containing user email, items, and shipping information
     * @return HTTP 201 Created with the created order’s status
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderStatus> createOrder(@RequestBody OrderDTO dto) {
        OrderStatus status = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    /**
     * Retrieves a single order by its ID.
     *
     * - Users can only access their own orders.
     * - Admins can access any order.
     *
     * @param id the order ID
     * @param email the email of the requester (used for permission check)
     * @return the full order DTO
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id, @RequestParam String email) {
        OrderDTO dto = orderService.getOrderById(id, email);
        return ResponseEntity.ok(dto);
    }

    /**
     * Returns all orders belonging to a specific customer.
     *
     * Only users with role USER may access this endpoint, and they may only view their own orders.
     *
     * @param email the email of the customer
     * @return list of orders for the user
     */
    @GetMapping("/user/{email}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderDTO>> getOrdersForCustomer(@PathVariable String email) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(email));
    }

    /**
     * Retrieves all orders in the system.
     *
     * Only users with role ADMIN can call this endpoint.
     *
     * @return list of all orders
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Updates the status of an order.
     *
     * Only admins can modify order statuses (e.g., PROCESSING, SHIPPED, COMPLETED).
     *
     * @param id the ID of the order
     * @param status the new status to apply
     * @return the updated order status
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderStatus> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        OrderStatus updated = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    /**
     * Allows a user to cancel their own order.
     *
     * Rules:
     * - Only users (ROLE_USER)
     * - User must own the order
     * - Order must still be cancelable (not shipped, etc.)
     *
     * @param id the order ID
     * @param email the email of the user requesting cancellation
     * @return updated order status after cancellation
     */
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderStatus> cancelOrder(
            @PathVariable Long id,
            @RequestParam String email) {

        OrderStatus status = orderService.cancelOrder(id, email);
        return ResponseEntity.ok(status);
    }

    /**
     * Deletes an order from the system.
     *
     * Only admins are allowed to delete orders.
     *
     * @param id the ID of the order to delete
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Allows a user to request a return for an order.
     *
     * Rules:
     * - Only ROLE_USER
     * - User must own the order
     * - Order must be eligible for return (e.g., delivered already)
     *
     * @param id the order ID
     * @param email the email of the user requesting return
     * @return the updated order status
     */
    @PatchMapping("/{id}/return")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderStatus> returnOrder(
            @PathVariable Long id,
            @RequestParam String email) {

        OrderStatus status = orderService.returnOrder(id, email);
        return ResponseEntity.ok(status);
    }
}
