package com.bugnbass.backend.controller;
import com.bugnbass.backend.model.enums.ProductCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bugnbass/api/shop")
public class CommonController {

    @GetMapping("/products/categories")
    public ProductCategory[] getCategories() {
        return ProductCategory.values();
    }
}
