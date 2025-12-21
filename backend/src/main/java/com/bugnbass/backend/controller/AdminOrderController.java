package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.OrderDto;
import com.bugnbass.backend.service.OrderService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST controller for managing orders in the admin context.
 * Accessible only by users with ROLE_ADMIN authority.
 */
@RestController
@RequestMapping("/admin/orders")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminOrderController {

    /**
     * Service handling order-related operations.
     */
    private final OrderService orderService;

    /**
     * Constructs the AdminOrderController with the required OrderService.
     *
     * @param orderService the OrderService instance
     */
    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Retrieves all orders.
     *
     * @return ResponseEntity containing a list of OrderDTO objects and HTTP status 200
     */
    @GetMapping()
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Updates an existing order.
     *
     * @param dto the order data transfer object containing updated information
     * @return ResponseEntity containing the updated OrderDTO and HTTP status 200
     */
    @PatchMapping("/update")
    public ResponseEntity<OrderDto> updateOrder(@RequestBody OrderDto dto) {
        return ResponseEntity.ok(orderService.updateOrder(dto));
    }

    /**
     * Deletes an order by its ID.
     *
     * @param id the order ID
     * @return ResponseEntity with HTTP status 204 (No Content)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
