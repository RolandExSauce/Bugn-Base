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
@RequestMapping("/bugnbass/api/user/orders")
@RequiredArgsConstructor
public class UserOrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderStatus> createOrder(@RequestBody OrderDTO dto) {
        OrderStatus status = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDTO> getOrderById(
            @PathVariable Long id,
            @RequestParam String email) {
        OrderDTO dto = orderService.getOrderById(id, email);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/customer/{email}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderDTO>> getOrdersForCustomer(@PathVariable String email) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(email));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderStatus> cancelOrder(
            @PathVariable Long id,
            @RequestParam String email) {
        OrderStatus status = orderService.cancelOrder(id, email);
        return ResponseEntity.ok(status);
    }

    @PatchMapping("/{id}/return")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderStatus> returnOrder(
            @PathVariable Long id,
            @RequestParam String email) {
        OrderStatus status = orderService.returnOrder(id, email);
        return ResponseEntity.ok(status);
    }
}