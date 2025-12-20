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
@RequestMapping("/user/orders")
@RequiredArgsConstructor
public class UserOrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<OrderStatus> createOrder(@RequestBody OrderDTO dto) {
        OrderStatus status = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO dto = orderService.getOrderById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/customer")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<OrderDTO>> getOrdersForCustomer() {
        List<OrderDTO> orders = orderService.getOrdersByCustomer();
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/cancel/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<OrderStatus> cancelOrder(@PathVariable Long id) {
        OrderStatus status = orderService.cancelOrder(id);
        return ResponseEntity.ok(status);
    }

    @PatchMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<OrderStatus> returnOrder(@PathVariable Long id) {
        OrderStatus status = orderService.returnOrder(id);
        return ResponseEntity.ok(status);
    }
}
