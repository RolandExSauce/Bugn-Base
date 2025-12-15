package com.bugnbass.backend.controller.admin;
import com.bugnbass.backend.dto.ProductDTO;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bugnbass/api/admin/")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable String id) {
        return adminProductService.getProduct(id);
    }

    @GetMapping("/products")
    public List<Product> getProducts () {
        return adminProductService.getProducts();
    }

    @PostMapping("/add-product")
    public ResponseEntity<Product> addProduct(@RequestBody ProductDTO newProduct) {
        Product saved = adminProductService.addProduct(newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        adminProductService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-product/{id}")
    public ResponseEntity<Void> updateProduct (@PathVariable String id, @RequestBody ProductDTO productDTO) {
        adminProductService.updateProduct(id, productDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
