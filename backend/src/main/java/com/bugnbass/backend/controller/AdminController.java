package com.bugnbass.backend.controller;


import com.bugnbass.backend.dto.ProductDTO;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.service.AdminProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
// @CrossOrigin(origins = "http://localhost:3000") // fürs frontend später
public class AdminController {
    private final AdminProductService adminProductService;

    public AdminController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        return adminProductService.getProduct(id);
    }

    @GetMapping
    public List<Product> getProducts () {
        return adminProductService.getProducts();
    }

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody ProductDTO newProduct) {
        adminProductService.addProduct(newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(String id) {
        adminProductService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct (@PathVariable String id, @RequestBody ProductDTO productDTO) {
        adminProductService.updateProduct(id, productDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
