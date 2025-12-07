package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.OrderDTO;
import com.bugnbass.backend.model.Order;
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

    // --------------------------------------------------------
    // 1) CREATE ORDER (nur USER)
    // --------------------------------------------------------
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderStatus> createOrder(@RequestBody OrderDTO dto) {
        OrderStatus status = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    // --------------------------------------------------------
    // 2) GET ORDER BY ID (User darf eigene sehen, Admin darf alle sehen)
    // --------------------------------------------------------
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id, @RequestParam String email) {
        OrderDTO dto = orderService.getOrderById(id, email);
        return ResponseEntity.ok(dto);
    }

    // --------------------------------------------------------
    // 3) GET ORDERS FOR A USER (nur für USER: eigene Orders)
    // --------------------------------------------------------
    @GetMapping("/user/{email}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderDTO>> getOrdersForCustomer(@PathVariable String email) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(email));
    }

    // --------------------------------------------------------
    // 4) GET ALL ORDERS (nur ADMIN)
    // --------------------------------------------------------
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // --------------------------------------------------------
    // 5) UPDATE STATUS (nur ADMIN)
    // --------------------------------------------------------
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderStatus> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        OrderStatus updated = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    // --------------------------------------------------------
    // 6) CANCEL ORDER (nur USER)
    // --------------------------------------------------------
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderStatus> cancelOrder(
            @PathVariable Long id,
            @RequestParam String email) {

        OrderStatus status = orderService.cancelOrder(id, email);
        return ResponseEntity.ok(status);
    }

    // --------------------------------------------------------
    // 7) DELETE ORDER (nur ADMIN)
    // --------------------------------------------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
