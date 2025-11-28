package com.bugnbass.backend.controller;
import com.bugnbass.backend.dto.ProductFilter;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.service.ProductService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/shop/products")
public class ProductController {

    private final ProductService userProductService;

    public ProductController(ProductService userProductService) {
        this.userProductService = userProductService;
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {

        return userProductService.getProduct(id);
    }

    @GetMapping
    public List<Product> getProducts (
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) Integer priceMin,
            @RequestParam(required = false) Integer priceMax,
            @RequestParam(required = false) List<String> brand,
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize
    ) {
        if (id != null) {
            return List.of(userProductService.getProduct(id));
        }

        return userProductService.getProducts(new ProductFilter(
                name,
                category,
                priceMin,
                priceMax,
                brand,
                pageNo,
                pageSize));
    }
}