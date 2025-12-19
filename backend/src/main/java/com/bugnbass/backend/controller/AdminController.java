package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.OrderDTO;
import com.bugnbass.backend.dto.ProductDTO;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.OrderStatus;
import com.bugnbass.backend.service.AdminService;
import com.bugnbass.backend.service.MediaService;
import com.bugnbass.backend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

  private final MediaService mediaService;
  private final AdminService adminService;
  private final OrderService orderService;

  public AdminController(MediaService mediaService,
                         AdminService adminService,
                         OrderService orderService) {
    this.mediaService = mediaService;
    this.adminService = adminService;
    this.orderService = orderService;
  }

  @GetMapping("/product/{id}")
  public Product getProduct(@PathVariable String id) {
    return adminService.getProduct(id);
  }

  @GetMapping("/products")
  public List<Product> getProducts () {
    return adminService.getProducts();
  }

  @PostMapping("/add-product")
  public ResponseEntity<Product> addProduct(@RequestBody ProductDTO newProduct) {
    Product saved = adminService.addProduct(newProduct);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @DeleteMapping("/delete-product/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
    adminService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/update-product/{id}")
  public ResponseEntity<Void> updateProduct (@PathVariable String id, @RequestBody ProductDTO productDTO) {
    adminService.updateProduct(id, productDTO);
    return ResponseEntity.status(HttpStatus.OK).build();
  }


  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<OrderDTO>> getAllOrders() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
    // Admin can get any order without email parameter
    OrderDTO dto = orderService.getOrderByIdForAdmin(id);
    return ResponseEntity.ok(dto);
  }

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<OrderStatus> updateOrderStatus(
      @PathVariable Long id,
      @RequestParam OrderStatus status) {
    OrderStatus updated = orderService.updateOrderStatus(id, status);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }
}
