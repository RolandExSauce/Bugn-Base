package com.bugnbass.backend.controller;

import com.bugnbass.backend.model.enums.ProductCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for common, publicly accessible shop-related endpoints.
 */
@RestController
@RequestMapping("/bugnbass/api/shop")
public class CommonController {

    /**
     * Retrieves all available product categories.
     *
     * @return an array of ProductCategory enums
     */
    @GetMapping("/products/categories")
    public ProductCategory[] getCategories() {
        return ProductCategory.values();
    }
}
