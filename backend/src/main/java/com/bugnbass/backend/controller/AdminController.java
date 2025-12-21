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
 * REST controller for admin-specific operations related to products.
 *
 * <p>Provides endpoints for retrieving, adding, updating, and deleting products.
 * All endpoints are secured and accessible only to users with ROLE_ADMIN authority.</p>
 */
@RestController
@RequestMapping("/bugnbass/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    /** Service handling media operations. */
    private final MediaService mediaService;

    /** Service handling admin-related operations. */
    private final AdminService adminService;

    /** Service handling order-related operations. */
    private final OrderService orderService;

    /**
     * Constructs the AdminController with the required services.
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
     * @param id the ID of the product
     * @return the {@link Product} object
     */
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable(name = "id") String id) {
        return adminService.getProduct(id);
    }

    /**
     * Retrieves all products.
     *
     * @return a list of {@link Product} objects
     */
    @GetMapping("/products")
    public List<Product> getProducts() {
        return adminService.getProducts();
    }

    /**
     * Adds a new product.
     *
     * @param newProduct the {@link ProductDto} containing product data
     * @return ResponseEntity containing the created {@link Product} and HTTP status 201
     */
    @PostMapping("/add-product")
    public ResponseEntity<Product> addProduct(@RequestBody ProductDto newProduct) {
        Product saved = adminService.addProduct(newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     * @return ResponseEntity with HTTP status 204 (No Content)
     */
    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") String id) {
        adminService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates an existing product by its ID.
     *
     * @param id the ID of the product to update
     * @param productDto the {@link ProductDto} containing updated product data
     * @return ResponseEntity with HTTP status 200 (OK)
     */
    @PutMapping("/update-product/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable(name = "id") String id,
            @RequestBody ProductDto productDto) {
        adminService.updateProduct(id, productDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
