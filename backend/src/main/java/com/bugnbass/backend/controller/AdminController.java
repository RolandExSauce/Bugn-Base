package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.ProductDto;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.service.AdminService;
import com.bugnbass.backend.service.MediaService;
import com.bugnbass.backend.service.OrderService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for admin-specific operations.
 * Only accessible by users with ROLE_ADMIN authority.
 * Provides endpoints for managing products.
 */
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    /**
     * Service handling media-related operations.
     */
    private final MediaService mediaService;

    /**
     * Service handling admin-related operations.
     */
    private final AdminService adminService;

    /**
     * Service handling order-related operations.
     */
    private final OrderService orderService;

    /**
     * Constructs the AdminController with required services.
     *
     * @param mediaService the MediaService instance
     * @param adminService the AdminService instance
     * @param orderService the OrderService instance
     */
    public AdminController(MediaService mediaService,
                           AdminService adminService,
                           OrderService orderService) {
        this.mediaService = mediaService;
        this.adminService = adminService;
        this.orderService = orderService;
    }

    /**
     * Retrieves a single product by its ID.
     *
     * @param id the product ID
     * @return the Product object
     */
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable String id) {
        return adminService.getProduct(id);
    }

    /**
     * Retrieves all products.
     *
     * @return a list of Product objects
     */
    @GetMapping("/products")
    public List<Product> getProducts() {
        return adminService.getProducts();
    }

    /**
     * Adds a new product.
     *
     * @param newProduct the product data transfer object
     * @return ResponseEntity containing the created Product and HTTP status 201
     */
    @PostMapping("/add-product")
    public ResponseEntity<Product> addProduct(@RequestBody ProductDto newProduct) {
        Product saved = adminService.addProduct(newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the product ID
     * @return ResponseEntity with HTTP status 204 (No Content)
     */
    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        adminService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates an existing product by its ID.
     *
     * @param id         the product ID
     * @param productDto the updated product data
     * @return ResponseEntity with HTTP status 200 (OK)
     */
    @PutMapping("/update-product/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable String id,
            @RequestBody ProductDto productDto) {
        adminService.updateProduct(id, productDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
