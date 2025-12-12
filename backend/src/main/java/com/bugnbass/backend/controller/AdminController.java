package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.ProductDTO;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.service.AdminProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling admin operations related to products. Provides endpoints to create, read,
 * update, and delete products.
 */
@RestController
@RequestMapping("/api/admin/")
@RequiredArgsConstructor
public class AdminController {

  private final AdminProductService adminProductService;

  /**
   * Retrieves a product by its ID.
   *
   * @param id the ID of the product
   * @return the product with the given ID
   */
  @GetMapping("/product/{id}")
  public Product getProduct(@PathVariable String id) {
    return adminProductService.getProduct(id);
  }

  /**
   * Retrieves all products.
   *
   * @return a list of all products
   */
  @GetMapping("/products")
  public List<Product> getProducts() {
    return adminProductService.getProducts();
  }

  /**
   * Adds a new product.
   *
   * @param newProduct the product data to create
   * @return the created product wrapped in a ResponseEntity with HTTP status 201
   */
  @PostMapping("/add-product")
  public ResponseEntity<Product> addProduct(@RequestBody ProductDTO newProduct) {
    Product saved = adminProductService.addProduct(newProduct);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  /**
   * Deletes a product by its ID.
   *
   * @param id the ID of the product to delete
   * @return a ResponseEntity with HTTP status 204 (No Content)
   */
  @DeleteMapping("/delete-product/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
    adminProductService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Updates an existing product.
   *
   * @param id the ID of the product to update
   * @param productDTO the updated product data
   * @return a ResponseEntity with HTTP status 200 (OK)
   */
  @PutMapping("/update-product/{id}")
  public ResponseEntity<Void> updateProduct(
      @PathVariable String id, @RequestBody ProductDTO productDTO) {
    adminProductService.updateProduct(id, productDTO);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
