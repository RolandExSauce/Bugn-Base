package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.ProductFilter;
import com.bugnbass.backend.dto.ProductResponseDto;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.service.ProductService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for publicly accessible product operations.
 * Provides endpoints to retrieve individual products or lists of products with optional filtering.
 */
@RestController
@RequestMapping("/bugnbass/api/products")
public class UserProductsController {

    private final ProductService productService;

    /**
     * Creates a new controller instance with the required product service.
     *
     * @param productService the product service
     */
    public UserProductsController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves a single product by its ID.
     *
     * @param id the product ID
     * @return the Product object
     */
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") Long id) {
        return productService.getProduct(id);
    }

    /**
     * Retrieves a list of products with optional filtering by name, category,
     * price range, brand, rating (stars), and pagination.
     *
     * @param name     optional product name filter
     * @param category optional product category filter
     * @param priceMin optional minimum price filter
     * @param priceMax optional maximum price filter
     * @param brand    optional list of brand filters
     * @param stars    optional minimum rating filter (e.g., 4 = 4 stars and above)
     * @param pageNo   optional page number for pagination (0-based)
     * @param pageSize optional page size for pagination
     * @return a list of ProductResponseDto objects matching the filters
     */
    @GetMapping
    public List<ProductResponseDto> getProducts(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category", required = false) ProductCategory category,
            @RequestParam(name = "priceMin", required = false) Integer priceMin,
            @RequestParam(name = "priceMax", required = false) Integer priceMax,
            @RequestParam(name = "brand", required = false) List<String> brand,
            @RequestParam(name = "stars", required = false) Integer stars,
            @RequestParam(name = "pageNo", required = false) Integer pageNo,
            @RequestParam(name = "pageSize", required = false) Integer pageSize
    ) {
        ProductFilter productFilters = new ProductFilter(
                name,
                category,
                priceMin,
                priceMax,
                brand,
                stars,
                pageNo,
                pageSize
        );

        return productService.getProducts(productFilters);
    }
}
