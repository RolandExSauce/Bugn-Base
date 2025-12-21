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
@RequestMapping("/products")
public class UserProductsController {

    /**
     * Service handling product-related operations.
     */
    private final ProductService userProductService;

    /**
     * Constructs the ProductsController with the required ProductService.
     *
     * @param userProductService the ProductService instance
     */
    public UserProductsController(ProductService userProductService) {
        this.userProductService = userProductService;
    }

    /**
     * Retrieves a single product by its ID.
     *
     * @param id the product ID
     * @return the Product object
     */
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return userProductService.getProduct(id);
    }

    /**
     * Retrieves a list of products with optional filtering by name, category,
     * price range, brand, and pagination.
     *
     * @param name     optional product name filter
     * @param category optional product category filter
     * @param priceMin optional minimum price filter
     * @param priceMax optional maximum price filter
     * @param brand    optional list of brand filters
     * @param pageNo   optional page number for pagination
     * @param pageSize optional page size for pagination
     * @return a list of ProductResponseDTO objects matching the filters
     */
    @GetMapping()
    public List<ProductResponseDto> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) Integer priceMin,
            @RequestParam(required = false) Integer priceMax,
            @RequestParam(required = false) List<String> brand,
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize
    ) {
        ProductFilter productFilters = new ProductFilter(
                name,
                category,
                priceMin,
                priceMax,
                brand,
                pageNo,
                pageSize);
        return userProductService.getProducts(productFilters);
    }
}
