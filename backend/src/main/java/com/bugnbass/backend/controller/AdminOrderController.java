package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.OrderDTO;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/order")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminOrderController {

  private final OrderService orderService;

  public AdminOrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("/orders")
  public ResponseEntity<List<OrderDTO>> getAllOrders() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @PatchMapping("/orders/{id}/status")
  public ResponseEntity<OrderStatus> updateOrderStatus(
      @PathVariable Long id,
      @RequestParam OrderStatus status) {
    return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
  }

  @DeleteMapping("/orders/{id}")
  public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }
}

