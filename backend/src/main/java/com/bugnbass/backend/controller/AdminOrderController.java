package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.OrderDTO;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminOrderController {

  private final OrderService orderService;

  public AdminOrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping()
  public ResponseEntity<List<OrderDTO>> getAllOrders() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @PatchMapping("/update")
  public ResponseEntity<OrderDTO> updateOrder(
      @RequestBody OrderDTO dto) {
    return ResponseEntity.ok(orderService.updateOrder(dto));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }
}

